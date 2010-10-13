package org.sc.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
		String sId = this.getSessionId(req);
		String unitName = req.getParameter("unitName");
		String loginName = req.getParameter("loginName");		
		MongoDbUtil.setCurrentInfo(unitName, sId, loginName);
		return true;
	}
	private String getSessionId(HttpServletRequest req){
		Cookie[] cs = req.getCookies();
		if(cs == null)
			return null;
		for(Cookie c : cs){
			if("_sessionId".equals(c.getName())){
				return c.getValue();
			}
		}
		return null;
	}
}
