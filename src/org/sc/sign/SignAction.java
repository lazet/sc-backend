package org.sc.sign;

import static org.sc.base.Constants.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.Cookie;

import org.sc.security.MongoDbUtil;
import org.sc.util.DateUtil;
import org.sc.util.EncryptUtil;
import org.sc.util.GeneralResult;
import org.sc.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sign")
public class SignAction {
	private String genSessionId(){
		return String.valueOf(Math.abs(UUID.randomUUID().hashCode()));
	}
	@RequestMapping(value="/authenticateServer", method=RequestMethod.POST )
	public @ResponseBody String authenticateServer(@RequestParam("loginName") String loginName, 
			@RequestParam(value="token", required=true) String token) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbUser =  new BasicDBObject();
		dbUser.put(LOGIN_NAME, loginName);
		DBObject dbo = dbc.findOne(dbUser);
		if(dbo != null){
			String password = (String)dbo.get("password");
			dbo.removeField("password");
			String sessionId = genSessionId();
			dbo.put(SESSION_ID,sessionId);
			//用md5生成加密串
			String serverKey = EncryptUtil.encodeWithMD5(loginName + token + sessionId + password + MIX_CODE);
			dbo.put(SERVER_KEY,serverKey);
			//用于第二次验证
			String clientKey = EncryptUtil.encodeWithMD5(loginName + serverKey + sessionId + password + MIX_CODE);
			
			DBCollection sessionCollection = MongoDbUtil.getSessionDb().getCollection(SESSION_INFO);
			DBObject session =  new BasicDBObject();
			session.put(SESSION_ID, sessionId);
			session.put(SERVER_KEY, serverKey);
			session.put(CLIENT_KEY, clientKey);
			session.put(STAGE, 1);
			session.put(PASSWORD, password);
			session.put(USER_INFO, dbo);
			session.put(DB_NAME, MongoDbUtil.getCurrentDbName());
			session.put(MERCHANT_ID, MongoDbUtil.getCurrentUnitName());
			session.put(LOGIN_NAME, loginName);
			session.put(START_TIME, DateUtil.getCurrentTime());
			sessionCollection.save(session);
			
			DBObject serverInfo =  new BasicDBObject();
			serverInfo.put(SESSION_ID,sessionId);
			serverInfo.put(SERVER_KEY,serverKey);
			return new GeneralResult("signOn.continue",serverInfo).toString();
		}
		else{
			return new GeneralResult("signOn.failed",StringUtil.toIso8859("用户名或密码不正确")).toString();
		}
	}
	@RequestMapping(value="/authenticateClient", method=RequestMethod.POST )
	public @ResponseBody String authenticateClient(
			@RequestParam("sessionId") String sessionId, 
			@RequestParam(value="clientKey", required=true) String clientKey) {
		DBCollection dbc = MongoDbUtil.getSessionDb().getCollection(SESSION_INFO);
		DBObject dbSession =  new BasicDBObject();
		dbSession.put(SESSION_ID, sessionId);
		DBObject dbo = dbc.findOne(dbSession);
		if(dbo != null){
			int stage = (Integer)dbo.get(STAGE);
			if(stage != 1){
				return new GeneralResult("signOn.failed",StringUtil.toIso8859("用户名或密码不正确")).toString();
			}
			String sessionClientKey = (String)dbo.get(CLIENT_KEY);
			if(sessionClientKey == null || !sessionClientKey.equals(clientKey)){
				dbo.put(STAGE, 0);
				dbc.save(dbo);
				return new GeneralResult("signOn.failed",StringUtil.toIso8859("用户名或密码不正确")).toString();
			}
			dbo.put(STAGE, 2);
			dbc.save(dbo);
			//获取用户信息
			DBObject userInfo = (DBObject)dbo.get(USER_INFO);
			//获取角色和权限
			DBCollection roleCollection = MongoDbUtil.getCurrentDb().getCollection(ROLE);
			DBObject roleCondition = new BasicDBObject();
			DBObject inCondition = new BasicDBObject();
			
			inCondition.put("$in",userInfo.get("roles")==null?new BasicDBList():userInfo.get("roles"));
			roleCondition.put("roleName", inCondition);
			roleCondition.put("merchantId", MongoDbUtil.getCurrentDbName());
			DBCursor cursor = roleCollection.find(roleCondition);
			List result = new BasicDBList();
			while(cursor.hasNext()){
				DBObject role = cursor.next();
				result.add(role);
			}
			userInfo.put("sessionId", sessionId);
			userInfo.put("roles",result);
			
			return new GeneralResult("signOn.success",userInfo).toString();
		}
		else{
			return new GeneralResult("signOn.failed",StringUtil.toIso8859("用户名或密码不正确")).toString();
		}
	}
}
