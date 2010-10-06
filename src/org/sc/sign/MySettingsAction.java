package org.sc.sign;

import static org.sc.base.Constants.LOGIN_NAME;
import static org.sc.base.Constants.USER;

import org.sc.security.MongoDbUtil;
import org.sc.util.GeneralResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;


@Controller
public class MySettingsAction {
	@RequestMapping("/mySettings/get")
	public @ResponseBody String getMySettings(@RequestParam("loginName") String loginName) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbUser =  new BasicDBObject();
		dbUser.put(LOGIN_NAME, loginName);
		DBObject dbo = dbc.findOne(dbUser);
		if(dbo != null){
			dbo.removeField("password");
			return new GeneralResult("getMySettings.success",dbo.toString()).toJson();
		}
		else{
			return new GeneralResult("getMySettings.failed","").toJson();
		}
	}
	@RequestMapping("/mySettings/save")
	public @ResponseBody String saveMySettings(@RequestParam("loginName") String loginName, @RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass,@RequestParam("trueName") String trueName, @RequestParam("mobile") String mobile, @RequestParam("email") String email) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbUser =  new BasicDBObject();
		dbUser.put(LOGIN_NAME, loginName);
		DBObject dbo = dbc.findOne(dbUser);
		if(dbo != null){
			if(oldPass.equals(dbo.get("password"))){
				dbo.put("password",newPass);
				dbo.put("trueName",trueName);
				dbo.put("mobile",mobile);
				dbo.put("email",email);
				dbc.save(dbo);
				return new GeneralResult("saveMySettings.success",dbo.toString()).toJson();
			}
			else{
				return new GeneralResult("saveMySettings.failed","密码不一致").toJson();
			}
		}
		else{
			return new GeneralResult("saveMySettings.failed","没找到对应的个人信息").toJson();
		}
	}
}
