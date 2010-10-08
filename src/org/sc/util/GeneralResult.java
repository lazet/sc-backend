package org.sc.util;

public class GeneralResult {
	public GeneralResult(String type, String content) {
		this.type = type;
		this.content = content;
	}
	private String type;
	private String content;
	public String toJson(){
		StringBuffer sb = new StringBuffer();
		if(this.content!=null && this.content.startsWith("{") && this.content.endsWith("}")){
			sb.append("{")
				.append("\"type\"").append(":").append("\"").append(type).append("\"")
				.append(",")
				.append("\"content\"").append(":").append(content)
				.append("}");
		}
		else if(this.content!=null && this.content.startsWith("[") && this.content.endsWith("]")){
			sb.append("{")
			.append("\"type\"").append(":").append("\"").append(type).append("\"")
			.append(",")
			.append("\"content\"").append(":").append(content)
			.append("}");
		}
		else{
			sb.append("{")
				.append("\"type\"").append(":").append("\"").append(type).append("\"")
				.append(",")
				.append("\"content\"").append(":").append("\"").append(content).append("\"")
				.append("}");
		}
		return sb.toString();
	}
}
