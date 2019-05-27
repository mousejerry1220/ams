package com.sinotrans.ams.common.message;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.utils.GZIPUtils;

public class InputMessageHandle {
	
	public static final ThreadLocal<String> threadLocalMessageId = new ThreadLocal<>();
	
	public static final ThreadLocal<String> threadLocalParamsType = new ThreadLocal<>();
	
	private final String inputParamsName = "params";
	
	private final String inputMessageIdName = "messageId";
	
	private final String inputParamsTypeName = "paramsType";
	
	private final String inputCompressFlag = "compressFlag";
	
	public String getInputParamsTypeName() {
		return inputParamsTypeName;
	}

	public String getInputParamsName() {
		return inputParamsName;
	}

	public String getInputMessageIdName() {
		return inputMessageIdName;
	}
	
	public void setMessageId(String messageId){
		threadLocalMessageId.set(messageId);
	}
	
	public String getParamsType(){
		return threadLocalParamsType.get();
	}
	
	public String getMessageId(){
		return threadLocalMessageId.get();
	}

	private JSONObject getParams(JSONObject inputParams) {
		JSONObject formatParams = null;
		String paramsType = inputParams.getString(getInputParamsTypeName());
		
		if(StringUtils.isEmpty(paramsType)){
			paramsType = "string";
		}
		
		if("string".equals(paramsType)){
			formatStringParams(inputParams, formatParams);
		}
		
		if("json".equals(paramsType)){
			formatJSONParams(inputParams);
		}
		
		return inputParams;
	}

	private void formatJSONParams(JSONObject inputParams) {
		JSONObject formatParams;
		formatParams = inputParams.getJSONObject(getInputParamsName());
		inputParams.clear();
		if(formatParams != null){
			inputParams.putAll(formatParams);
		}
	}

	private void formatStringParams(JSONObject inputParams, JSONObject formatParams) {
		String _params = inputParams.getString(getInputParamsName());
		String compressFlag = inputParams.getString(inputCompressFlag);
		if("Y".equals(compressFlag)){
			_params = decompression(_params);
		}
		
		inputParams.clear();
		try{
			formatParams = JSON.parseObject(_params);
		}catch (Exception e) {
			throw new IllegalArgumentException("必须传入json格式的参数");
		}
		if(formatParams != null){
			inputParams.putAll(formatParams);
		}
	}
	
	private String decompression(String stringParams) {
		if(stringParams == null){
			return null;
		}
		byte[] bytes = Base64.decodeBase64(stringParams.getBytes());
		return new String(GZIPUtils.uncompress(bytes));
	}

	public JSONObject handle(JSONObject inputParams) {
		String messageId = inputParams.getString(getInputMessageIdName());
		threadLocalMessageId.set(messageId);
		threadLocalParamsType.set(inputParams.getString(getInputParamsTypeName()));
		return getParams(inputParams);
	}

	public void destroyThreadLocal(Object object) {
		threadLocalMessageId.set(null);
		threadLocalParamsType.set(null);
	}
	
}
