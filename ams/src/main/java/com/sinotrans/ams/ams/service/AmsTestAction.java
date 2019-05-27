package com.sinotrans.ams.ams.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.message.ResponseError;
import com.sinotrans.ams.common.message.ResponseSuccess;
import com.sinotrans.ams.common.sqler.ScriptService;


@RestController
public class AmsTestAction {

	@Autowired
	ScriptService scriptService;
	
	@RequestMapping("/test")
	public Object amsTestService(@RequestBody JSONObject params){
		
		return new ResponseError(100002, "错误原因");
		
//		return new ResponseSuccess(scriptService.execute("a"));
	}
	
}
