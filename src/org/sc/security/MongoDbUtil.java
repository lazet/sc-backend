package org.sc.security;

import static org.sc.base.Constants.BASE_DB;
import static org.sc.base.Constants.MERCHANT;
import static org.sc.base.Constants.MERCHANT_ID;

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
	
	public static boolean setCurrentInfo(String unitName){
		if(unitName == null || BASE_DB.equalsIgnoreCase(unitName)){
			return false;
		}
		DB db = mongo.getDB(BASE_DB);
		DBObject o = db.getCollection(MERCHANT).findOne(new BasicDBObject(MERCHANT_ID,unitName));
		if (o!= null){
			Map<String,Object> info = new HashMap<String,Object>();
			info.put("DB", mongo.getDB(unitName));
			info.put("UnitName", unitName);
			tl.set(info);
			return true;
		}
		else{
			return false;
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
		return (DB)tl.get().get("DB");
	}
	public static String getCurrentUnitName(){
		if(tl.get()==null)
			return null;
		return (String)tl.get().get("UnitName");
	}
}
