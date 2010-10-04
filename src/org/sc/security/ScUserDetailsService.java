package org.sc.security;

import static org.sc.base.Constants.*;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * 收银权限管理
 * @author admin
 *
 */
public class ScUserDetailsService implements UserDetailsService,InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public UserDetails loadUserByUsername(String userId)
			throws UsernameNotFoundException, DataAccessException {
		if (userId == null)
			throw new UsernameNotFoundException("userId is null");
		if(MongoDbUtil.getCurrentDb() == null)
			throw new UsernameNotFoundException("unitName is null");
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbUser =  new BasicDBObject();
		dbUser.put(USER_ID, userId);
		DBObject dbo = dbc.findOne(dbUser);
		if(dbo == null)
			throw new UsernameNotFoundException("user is not existed:" + userId);
		return new User(userId, (String)dbo.get(USER_PASSWD), (Boolean)dbo.get(ENABLED), true, true, true, AuthorityUtils.NO_AUTHORITIES);
	}

}
