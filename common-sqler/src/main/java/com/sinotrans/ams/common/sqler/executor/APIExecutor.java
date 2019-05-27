package com.sinotrans.ams.common.sqler.executor;

import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.utils.StringUtils;

public abstract class APIExecutor {

	protected ScriptDefinition daoDef;
	
	protected APIExecutor(ScriptDefinition daoDef){
		this.daoDef = daoDef;
	}

	public Object exec(ExecuteContext context){
		
		//设置最后更新时间
		if(daoDef.getUpdateTimestamp() != null){
			context.getParams().put("_updateTimestamp", daoDef.getUpdateTimestamp());
		}
		
		String script =StringUtils.processTemplate(daoDef.getScriptContent(), context.getParams());
		
		return execute(script,context);
	}
	
	public abstract Object execute(String script,ExecuteContext context);
	
	protected <T> T getValue(JSONObject params,String key,Class<T> clazz) {
		if(key == null){
			return null;
		}
		Set<Entry<String, Object>> entrys = params.entrySet();
		for(Entry<String, Object> entry : entrys){
			if(key.equalsIgnoreCase(entry.getKey())){
				return params.getObject(entry.getKey(), clazz);
			}
		}
		return null;
	}
}
