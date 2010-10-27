package org.sc.util;

import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;

public class GeneralResult {
	public GeneralResult(String type, Object content) {
		this.type = type;
		this.content = content;
	}
	private String type;
	private Object content;
	@Override 
	public String toString(){
		BasicDBObject bdbo = new BasicDBObject();
		bdbo.append("type", type)
			.append("content", content);
		return bdbo.toString();
	}
}
