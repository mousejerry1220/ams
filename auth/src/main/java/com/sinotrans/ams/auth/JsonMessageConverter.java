package com.sinotrans.ams.auth;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

/**
 * 
 * @author Jerry.Zhao
 * 将正常返回的参数编码为{status='error',message='返回值'}的形式
 * 
 */
public class JsonMessageConverter extends FastJsonHttpMessageConverter4 {
	
	public static String STATUS = "status";
	
	public static String MESSAGE = "message";
	
	@Override
	protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) 
			throws IOException, HttpMessageNotWritableException {
		
		//成功返回token
		if(obj instanceof DefaultOAuth2AccessToken){
			Map<String,Object> r = new HashMap<String,Object>();
			DefaultOAuth2AccessToken token = ((DefaultOAuth2AccessToken)obj);
			token.getRefreshToken();
			r.put("access_token",token.getValue());
			r.put("refresh_token",token.getRefreshToken().getValue());
			r.put("expires_in",token.getExpiresIn());
			r.put(STATUS, 0);
			super.writeInternal(r, type, outputMessage);
			return;
		}
		
		if(obj instanceof OAuth2Exception){
			String errorMessage = ((OAuth2Exception)obj).getMessage();
			Map<String,Object> r = new HashMap<String,Object>();
			r.put(STATUS, 400);
			r.put(MESSAGE,  ErrorMapping.getMessage(errorMessage));
			super.writeInternal(r, type, outputMessage);
			return;
		}
		
//		if((obj instanceof Map) && ((Map)obj).get("status") != null && (Integer)((Map)obj).get("status") == 401){
//			Map<String,Object> r = new HashMap<String,Object>();
//			r.put(MESSAGE, "无权访问");
//			r.put(STATUS, 401);
//			super.writeInternal(r, type, outputMessage);
//			return;
//		}
		
		super.writeInternal(obj, type, outputMessage);
	}
	
	@Override
	protected void addDefaultHeaders(HttpHeaders headers, Object t, MediaType contentType)
			throws IOException {
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.set("Access-Control-Allow-Origin", "*");
		headers.set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		super.addDefaultHeaders(headers, t, contentType);
	}
	
}
