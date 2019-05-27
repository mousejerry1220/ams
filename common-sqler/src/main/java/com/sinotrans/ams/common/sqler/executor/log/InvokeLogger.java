package com.sinotrans.ams.common.sqler.executor.log;

import java.util.Date;

import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;

public class InvokeLogger {
	
	Date createDate;
	String scriptCode;
	long expendTime;
	String params;
	String userId;
	String error;
	String scriptContent;
	
	public InvokeLogger(ScriptDefinition daoDef,String params,long expendTime,String error){
		this.createDate = new Date();
		this.scriptCode = daoDef.getScriptCode();
		this.expendTime = expendTime;
		this.params = params;
		this.error = error;
		this.scriptContent = daoDef.getScriptContent();
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public String getAppCode() {
		return scriptCode;
	}

	public long getExpendTime() {
		return expendTime;
	}

	public String getParams() {
		return params;
	}

	public String getUserId() {
		return userId;
	}

	public String getError() {
		return error;
	}

	public String getScriptCode() {
		return scriptCode;
	}

	public String getScriptContent() {
		return scriptContent;
	}
	
}
