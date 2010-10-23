package org.sc.util;

public class StringUtil {
	public static String toIso8859(String normalString){
		if(normalString == null)
			return null;
		try{
			return new String(normalString.getBytes("utf-8"),"iso-8859-1");
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
