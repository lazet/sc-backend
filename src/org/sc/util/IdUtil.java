package org.sc.util;

import java.util.UUID;
import java.lang.Math;

import org.apache.commons.lang.StringUtils;

public class IdUtil {
	static public String newId(){
		String id = StringUtils.leftPad(String.valueOf(Math.abs(UUID.randomUUID().hashCode())),10,'0');
		return id.substring(id.length() - 10, id.length());
	}
}
