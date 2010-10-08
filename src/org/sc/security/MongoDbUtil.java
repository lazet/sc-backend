package org.sc.security;

import static org.sc.base.Constants.*;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 数据库工具类，可以获得当前数据库
 * @author admin
 *
 */
public class MongoDbUtil {
	private static ThreadLocal<Map<String,Object>> tl = new ThreadLocal<Map<String,Object>>();
	
	public static void setCurrentInfo(String unitName, String sessionId, String loginName){
		if( sessionId == null &&(unitName == null || BASE_DB.equalsIgnoreCase(unitName))){
			return;
		}
		Map<String,Object> info = new HashMap<String,Object>();
		tl.set(info);

		if (sessionId != null){
			DB sessionDb = mongo.getDB(SESSION_DB);
			DBObject sessionInfo = sessionDb.getCollection(SESSION_INFO).findOne(new BasicDBObject(SESSION_ID,sessionId));
			if (sessionInfo!= null){
				String sessionUnitName = (String)sessionInfo.get(MERCHANT_ID);
				
				info.put(MERCHANT_ID, sessionUnitName);
				info.put(DB_NAME, sessionInfo.get(DB_NAME));
				//DBObject unitInfo = baseDb.getCollection(MERCHANT).findOne(new BasicDBObject(MERCHANT_ID,sessionUnitName));
				//info.put(DB_NAME, unitInfo.get(DB_NAME));
				info.put(LOGIN_NAME, (String)sessionInfo.get(LOGIN_NAME));
			}
		}
		if(unitName != null){
			//忽视SessionInfo,一般在登录和修改个人信息设定起效.
			DB baseDb = mongo.getDB(BASE_DB);
			DBObject unitInfo = baseDb.getCollection(MERCHANT).findOne(new BasicDBObject(MERCHANT_ID,unitName));
			if (unitInfo!= null){				
				info.put(MERCHANT_ID, unitName);
				info.put(DB_NAME, unitInfo.get(DB_NAME));
				info.put(LOGIN_NAME, loginName);
			}
		}
	}
	
	private static  Mongo mongo = null;
	public static void setMongo(Mongo m){
		if(mongo == null)
			mongo = m;
	}
	public static DB getCurrentDb(){
		if(tl.get()==null)
			return mongo.getDB("m1");
		return mongo.getDB((String)tl.get().get(DB_NAME));
	}
	public static DB getBaseDb(){
		return mongo.getDB(BASE_DB);
	}
	public static DB getSessionDb(){
		return mongo.getDB(SESSION_DB);
	}
	public static String getCurrentUnitName(){
		if(tl.get()==null)
			return null;
		return (String)tl.get().get(MERCHANT_ID) ;
	}
	public static String getCurrentDbName(){
		if(tl.get()==null)
			return null;
		return (String)tl.get().get(DB_NAME) ;
	}
	public static String getCurrentLoginName(){
		if(tl.get()==null)
			return null;
		return (String)tl.get().get(LOGIN_NAME);
	}
}
