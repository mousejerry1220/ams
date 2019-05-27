package com.sinotrans.ams.auth;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapping {

	static Map<String,String> errorMapping = new HashMap<String,String>();
	
	static{
		errorMapping.put("Bad credentials", "用户名或者密码错误");
	}
	
	public static String getMessage(String message){
		return errorMapping.get(message) == null ? message : errorMapping.get(message);
	}
	
}
