package com.sinotrans.ams.common.message;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.sinotrans.ams.common.utils.GZIPUtils;

/**
 * 
 * @author Jerry.Zhao
 * 将正常返回的参数编码为{status='error',message='返回值'}的形式
 * 
 */
public class JsonMessageConverter extends FastJsonHttpMessageConverter4 {
	
	public static final String STATUS = "status";
	
	public static final String MESSAGE = "message";
	
	public static final String ERROR = "error";
	
	public static final int SUCCESS_CODE = 0;
	
	@Autowired
	InputMessageHandle inputMessageHandle;
	
	@Override
	protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) 
			throws IOException, HttpMessageNotWritableException {
		Map<String,Object> r = new HashMap<String,Object>();
		String messageId = inputMessageHandle.getMessageId();
		
		String paramsType = inputMessageHandle.getParamsType();
		
		String returnCompressFlag = inputMessageHandle.getReturnCompressFlag();
		
		inputMessageHandle.destroyThreadLocal(null);
		
		if(messageId != null){
			r.put("messageId", messageId);
		}
		
		if(paramsType != null){
			r.put("messageType", paramsType);
		}
		
		if(obj instanceof ResponseSuccess){
			onSuccess((ResponseSuccess)obj, type, outputMessage, r , paramsType, returnCompressFlag);
			return ;
		}
		
		if(obj instanceof ResponseError){
			onError((ResponseError)obj, type, outputMessage, r);
			return ;
		}
		
		if(onErrorCase(obj, type, outputMessage,r)){
			return ;
		}
		
		super.writeInternal(obj, type, outputMessage);
	}

	private void onError(ResponseError obj, Type type, HttpOutputMessage outputMessage, Map<String, Object> r)
			throws IOException {
		r.put(STATUS, obj.getCode());
		r.put(MESSAGE, obj.getMessage());
		super.writeInternal(r, type, outputMessage);
	}

	private void onSuccess(ResponseSuccess obj, Type type, HttpOutputMessage outputMessage, Map<String, Object> r,String paramsType , String returnCompressFlag) throws IOException {
		r.put(STATUS, SUCCESS_CODE);
		if(!StringUtils.isEmpty(obj)){
			Object message = obj;
			if(!"json".equalsIgnoreCase(paramsType)){
				message = JSON.toJSONString(obj.getResult());
				if("Y".equals(returnCompressFlag) && message!=null && ((String)message).getBytes().length > 1024){
					message = Base64.encodeBase64String(GZIPUtils.compress(((String)message).getBytes()));
					r.put("compressFlag", "Y");
				}
			}
			r.put(MESSAGE, message);
		}
		super.writeInternal(r, type, outputMessage);
	}

	@SuppressWarnings("rawtypes")
	private boolean onErrorCase(Object obj, Type type, HttpOutputMessage outputMessage,Map<String,Object> r) throws IOException {
		if((obj instanceof Map) 
				&& ((Map)obj).get("status") != null 
				&& ((Map)obj).get("error") != null){
			r.put(STATUS,((Map)obj).get("status"));
			r.put(ERROR, ((Map)obj).get("message"));
			super.writeInternal(r, type, outputMessage);
			return true;
		}
		return false;
	}
	
	@Override
	protected void addDefaultHeaders(HttpHeaders headers, Object t, MediaType contentType)
			throws IOException {
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//		headers.set("Access-Control-Allow-Origin", "*");
		headers.set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		super.addDefaultHeaders(headers, t, contentType);
	}
	
}
