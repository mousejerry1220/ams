package com.sinotrans.ams.common.sqler.executor.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.sqler.ScriptService;
import com.sinotrans.ams.common.sqler.executor.APIExecutor;
import com.sinotrans.ams.common.sqler.executor.ExecuteContext;
import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;

public class SearchLinkExecutor extends APIExecutor{

	public SearchLinkExecutor(ScriptDefinition daoDef) {
		super(daoDef);
	}

	@Override
	public Object execute(String script,ExecuteContext context) {
		ScriptService scriptService = context.getScriptService();
		JSONObject params = context.getParams();

		JSONObject rule = JSON.parseObject(script);
		
		String mainScriptCode = rule.getString("mainScriptCode");
		JSONArray childs = rule.getJSONArray("childs");
		Object mainObject = scriptService.execute(mainScriptCode, params);
		if(mainObject == null){
			return null;
		}
		
		JSONObject main = null;
		try{
			main = JSON.parseObject(JSON.toJSONString(mainObject));
		}catch (Exception e) {
			throw new RuntimeException("主对象必须是SEARCH_OBJECT类型");
		}
		
		for(int i=0;i<childs.size();i++){
			JSONObject childRule = childs.getJSONObject(i);
			String childScriptCode = childRule.getString("childScriptCode");
			String childListName = childRule.getString("childListName");
			Object childObject = scriptService.execute(childScriptCode, params);
			main.put(childListName, childObject);
		}
		
		return main;
	}

}
