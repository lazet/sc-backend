package org.sc.user;

import static org.sc.base.Constants.CREATED_TIME;
import static org.sc.base.Constants.CREATOR;
import static org.sc.base.Constants.ID;
import static org.sc.base.Constants.KEYWORDS;
import static org.sc.base.Constants.MERCHANT_ID;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sc.resources.ConsumeRecordAction;
import org.sc.security.MongoDbUtil;
import org.sc.util.DateUtil;
import org.sc.util.GeneralResult;
import org.sc.util.IdUtil;
import org.sc.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * 用户操作，指定单位（商户或门店），指定组（可以有多个）
 * @author admin
 *
 */
@Controller
@RequestMapping("/user")
public class UserAction {
	static public String ROLE = "role";
	static public String USER = "user";
	static public String PK = "loginName";
	
	static public Log logger = LogFactory.getLog(ConsumeRecordAction.class);
	
	@RequestMapping("/add")
	public @ResponseBody String add(@RequestParam("user") String user) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbo = (DBObject)JSON.parse(user);
		//检查是否有重复的
		DBObject condition =  new BasicDBObject();
		condition.put(PK, dbo.get(PK));
		DBObject r =dbc.findOne(condition);
		if(r != null){
			return new GeneralResult("addUser.failed",StringUtil.toIso8859("用户名已使用，请换用其他名字")).toString();
		}
		//开始增加用户操作
		dbo.put(MERCHANT_ID, MongoDbUtil.getCurrentUnitName());
		dbo.put(CREATED_TIME, DateUtil.getCurrentTime());
		dbo.put(CREATOR, MongoDbUtil.getCurrentLoginName());
		dbo.put(KEYWORDS, generatedKeyWords(dbo,new HashSet<String>()));
		dbc.save(dbo);
		return new  GeneralResult("addUser.success" ,dbo).toString();
	}
	@RequestMapping("/update")
	public @ResponseBody String update(@RequestParam("user") String user) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbo = (DBObject)JSON.parse(user);
		//检查是否有重复的
		DBObject condition =  new BasicDBObject();
		condition.put(PK, dbo.get(PK));
		DBObject r =dbc.findOne(condition);
		if(r == null){
			return new GeneralResult("updateUser.failed",StringUtil.toIso8859("找不到原用户")).toString();
		}
		r.put("roles", dbo.get("roles"));
		r.put("email", dbo.get("email"));
		r.put("mobile", dbo.get("mobile"));
		r.put("trueName", dbo.get("trueName"));
		
		r.put(KEYWORDS, generatedKeyWords(r,new HashSet<String>()));
		dbc.save(r);
		return new  GeneralResult("updateUser.success" ,r).toString();
	}
	protected Set<String> generatedKeyWords(DBObject dbo,Set<String> unindexItemNames){
		Set<String> keySet = dbo.keySet();
		Set<String> keywords = new HashSet<String>(); 
		for(String key:keySet){
			if(KEYWORDS.equals(key) || "_id".equals(key) || unindexItemNames.contains(key) || "mx_internal_uid".equals(key) || MERCHANT_ID.equals(key))
				continue;
			Object v = dbo.get(key);
			if(v == null)
				continue;
			else{
				if(v instanceof Collection){
					Collection c = (Collection)v;
					for(Object o: c){
						if(o == null){
							continue;
						}else if(o instanceof DBObject){
							keywords.addAll(generatedKeyWords((DBObject)o,unindexItemNames));
						}
						else{
							keywords.add(o.toString());
						}
					}
				}
				else if( v instanceof DBObject){
					keywords.addAll(generatedKeyWords((DBObject)v,unindexItemNames));
				}
				else{
					String value = String.valueOf(v);
					if("".equals(value))
						continue;
					keywords.add(value);
				}
			}
		}
		return keywords;
	}
	@RequestMapping("/searchkeywords")
	public @ResponseBody String searchKeywords(@RequestParam("condition") String condition){
		DBCollection dc = MongoDbUtil.getCurrentDb().getCollection(USER);
		DBObject dbcondition = (DBObject)JSON.parse(condition);
		List result = dc.distinct(KEYWORDS, dbcondition);
		return  new GeneralResult("user.searchkeywords.success",result).toString();
	}
}
