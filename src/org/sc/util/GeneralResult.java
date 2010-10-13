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
//	public String toJson(){	
//		StringBuffer sb = new StringBuffer();
//		if(this.content!=null && this.content.startsWith("{") && this.content.endsWith("}")){
//			sb.append("{")
//				.append("\"type\"").append(":").append("\"").append(type).append("\"")
//				.append(",")
//				.append("\"content\"").append(":").append(content)
//				.append("}");
//		}
//		else if(this.content!=null && this.content.startsWith("[") && this.content.endsWith("]")){
//			sb.append("{")
//			.append("\"type\"").append(":").append("\"").append(type).append("\"")
//			.append(",")
//			.append("\"content\"").append(":").append(content)
//			.append("}");
//		}
//		else{
//			sb.append("{")
//				.append("\"type\"").append(":").append("\"").append(type).append("\"")
//				.append(",")
//				.append("\"content\"").append(":").append("\"").append(content).append("\"")
//				.append("}");
//		}
//		return sb.toString();
//	}
}
