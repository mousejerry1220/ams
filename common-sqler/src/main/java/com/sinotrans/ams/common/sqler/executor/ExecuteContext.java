package com.sinotrans.ams.common.sqler.executor;

import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.sqler.ScriptService;

public class ExecuteContext {

	public ExecuteContext(JSONObject params , ScriptService scriptService) {
		
		this.params = params;
		
		this.scriptService = scriptService;
	}
	
	JSONObject params;
	
	ScriptService scriptService;

	public JSONObject getParams() {
		return params;
	}

	public ScriptService getScriptService() {
		return scriptService;
	}
	
}
