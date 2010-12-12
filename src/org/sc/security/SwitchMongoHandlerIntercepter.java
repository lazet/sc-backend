package org.sc.security;

import static org.sc.base.Constants.CLIENT_KEY;
import static org.sc.base.Constants.PASSWORD;
import static org.sc.base.Constants.ROLE;
import static org.sc.base.Constants.SESSION_ID;
import static org.sc.base.Constants.SESSION_INFO;
import static org.sc.base.Constants.STAGE;
import static org.sc.base.Constants.USER_INFO;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sc.base.Constants;
import org.sc.util.EncryptUtil;
import org.sc.util.GeneralResult;
import org.sc.util.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SwitchMongoHandlerIntercepter implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object handler) throws Exception {
		String sId = req.getParameter("sessionId");
		String unitName = req.getParameter("unitName");
		String loginName = req.getParameter("loginName");		
		MongoDbUtil.setCurrentInfo(unitName, sId, loginName);
		//安全校验
		String url = req.getRequestURL().toString();
		if(url.indexOf("/sign/") == -1){
			//检查session
			if(sId == null){
				return false;
			}
			//检查session状态
			DBCollection dbc = MongoDbUtil.getSessionDb().getCollection(SESSION_INFO);
			DBObject dbSession =  new BasicDBObject();
			dbSession.put(SESSION_ID, sId);
			DBObject dbo = dbc.findOne(dbSession);
			if(dbo != null){
				int stage = (Integer)dbo.get(STAGE);
				if(stage != 2){
					return false;
				}

				String p = (String)dbo.get(PASSWORD);
				if(p == null){
					return false;
				}
				//加密并生成验证信息
				long encryptCode = 0;
				Enumeration e = req.getParameterNames();
				while(e.hasMoreElements()){
					String prop = (String)e.nextElement();
					if("sessionId".equals(prop) || "encryptCode".equals(prop))
						continue;
					String hexMd5 = EncryptUtil.encodeWithMD5(StringUtil.fromIso8859(prop) + StringUtil.fromIso8859(req.getParameter(prop)));
					String shortHex = hexMd5.substring(0,6);
					long value = Long.parseLong(shortHex.toUpperCase(),16);
					encryptCode = encryptCode + value;
				}
				//用password加密
				String appendMd5 = EncryptUtil.encodeWithMD5(StringUtil.fromIso8859(p) + sId + Constants.MIX_CODE);
				String appendHex = appendMd5.substring(0,6);
				long appendValue = Long.parseLong(appendHex.toUpperCase(),16);
				encryptCode = encryptCode + appendValue;
				if(encryptCode == Long.parseLong(req.getParameter("encryptCode")))
					return true;
				else
					return false;

			}
			else{
				return false;
			}
		}
		return true;
	}

}
