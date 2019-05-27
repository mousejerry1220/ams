package com.sinotrans.ams.ams.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.message.ResponseResult;
import com.sinotrans.ams.common.message.ResponseSuccess;
import com.sinotrans.ams.common.sqler.ScriptService;

@RestController
public class AssetDataService {

	@Autowired
	ScriptService scriptService;
	
	@RequestMapping("/data/{scriptCode}")
	public ResponseResult addService(
			@RequestBody(required=false) JSONObject params,
			@PathVariable(value="scriptCode",required=true) String scriptCode
			){
		return new ResponseSuccess(scriptService.execute(scriptCode,params));
	}
	
}
