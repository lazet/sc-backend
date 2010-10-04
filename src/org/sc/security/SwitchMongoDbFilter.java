package org.sc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.filter.GenericFilterBean;

/**
 * 切换数据源，凡是具体到某个商户的请求均需要进行数据源切换
 * @author admin
 *
 */
public class SwitchMongoDbFilter extends GenericFilterBean  implements InitializingBean {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		String unitName = req.getParameter("unitName");
		MongoDbUtil.setCurrentInfo(unitName);
		chain.doFilter(req, resp);
	}

	
}
