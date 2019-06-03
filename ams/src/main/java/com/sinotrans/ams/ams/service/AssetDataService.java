package com.sinotrans.ams.ams.service;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.message.ResponseError;
import com.sinotrans.ams.common.message.ResponseResult;
import com.sinotrans.ams.common.message.ResponseSuccess;
import com.sinotrans.ams.common.sqler.ScriptService;

@CrossOrigin
@RestController
public class AssetDataService {

	@Autowired
	ScriptService scriptService;
	
	@RequestMapping(value="/data/{scriptCode}")
	public ResponseResult dataService(
			@RequestBody(required=false) JSONObject params,
			@PathVariable(value="scriptCode",required=true) String scriptCode){
		params = setParams(params);
		return new ResponseSuccess(scriptService.execute(scriptCode,params));
	}

	private JSONObject setParams(JSONObject params) {
		if(params == null){
			params = new JSONObject();
		}
		params.putAll(getUserMap());
		return params;
	}

	private JSONObject getUserMap(){
		Principal user = SecurityContextHolder.getContext().getAuthentication();
		Map<?,?> map = (Map<?, ?>) ((OAuth2Authentication)user).getUserAuthentication().getDetails();
		JSONObject userMap = new JSONObject();
		userMap.put("username", user.getName());
		userMap.put("duty", map.get("duty"));
		userMap.put("language", map.get("language"));
		return userMap;
	}
	
	@RequestMapping(value="/batch/searchList")
	public ResponseResult searchListService(@RequestBody(required=false) JSONObject params){
		params = setParams(params);
		JSONArray result = new JSONArray();
		Principal user = SecurityContextHolder.getContext().getAuthentication();
		params.put("username", user.getName());
		JSONArray keys = params.getJSONArray("keys");
		if(keys == null || keys.size() == 0){
			return new ResponseError(10100, "keys不能为空");
		}
		for(Object _key : keys){
			String key = (String)_key;
			Object list = scriptService.execute(key,params);
			JSONObject item = new JSONObject();
			item.put("key", key);
			item.put("list", list);
			result.add(item);
		}
		return new ResponseSuccess(result);
	}
	
}
