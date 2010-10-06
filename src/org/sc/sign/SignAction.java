package org.sc.sign;

import static org.sc.base.Constants.USER;
import static org.sc.base.Constants.LOGIN_NAME;

import org.sc.security.MongoDbUtil;
import org.sc.util.GeneralResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/sign")
public class SignAction {
	@RequestMapping("/signOn")
	public @ResponseBody String signOn(@RequestParam("loginName") String loginName, @RequestParam("password") String token ) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbUser =  new BasicDBObject();
		dbUser.put(LOGIN_NAME, loginName);
		DBObject dbo = dbc.findOne(dbUser);
		if(dbo != null && token.equals(dbo.get("password"))){
			dbo.removeField("password");
			return new GeneralResult("signOn.success",dbo.toString()).toJson();
		}
		else{
			return new GeneralResult("signOn.failed","密码不正确").toJson();
		}
	}

}
