package org.sc.sign;

import static org.sc.base.Constants.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.Cookie;

import org.sc.security.MongoDbUtil;
import org.sc.util.DateUtil;
import org.sc.util.GeneralResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/sign")
public class SignAction {
	@RequestMapping(value="/signOn", method=RequestMethod.POST )
	public @ResponseBody String signOn(@RequestParam("loginName") String loginName, 
			@RequestParam("password") String token,
			HttpServletRequest  request,
			HttpServletResponse response) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbUser =  new BasicDBObject();
		dbUser.put(LOGIN_NAME, loginName);
		DBObject dbo = dbc.findOne(dbUser);
		if(dbo != null && token.equals(dbo.get("password"))){
			dbo.removeField("password");
			String sessionId = genSessionId(request);
			Cookie c = new Cookie("_sessionId", sessionId);
			c.setPath(request.getContextPath());
			c.setMaxAge(-1);
			response.addCookie(c);
			DBCollection sessionCollection = MongoDbUtil.getSessionDb().getCollection(SESSION_INFO);
			DBObject session =  new BasicDBObject();
			session.put(SESSION_ID, sessionId);
			session.put(DB_NAME, MongoDbUtil.getCurrentDbName());
			session.put(MERCHANT_ID, MongoDbUtil.getCurrentUnitName());
			session.put(LOGIN_NAME, loginName);
			session.put("startTime", DateUtil.getCurrentTime());
			sessionCollection.save(session);
			return new GeneralResult("signOn.success",dbo).toString();
		}
		else{
			return new GeneralResult("signOn.failed","密码不正确").toString();
		}
	}
	private String genSessionId(HttpServletRequest request){
		return String.valueOf(UUID.randomUUID().hashCode());
	}
}
