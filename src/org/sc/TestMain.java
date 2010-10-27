package org.sc;

import java.util.regex.Pattern;  
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

public class TestMain {
	public static void main(String[] argv){
		Pattern pattern = Pattern.compile("^.*" + "Mon"+ ".*$", Pattern.CASE_INSENSITIVE); 
		BasicDBObject bdo = new BasicDBObject();
		bdo.put("aa", pattern);
		System.out.println(JSON.serialize(bdo));
	}
}
