package com.sinotrans.ams.ams.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.message.ResponseError;
import com.sinotrans.ams.common.message.ResponseResult;
import com.sinotrans.ams.common.message.ResponseSuccess;
import com.sinotrans.ams.common.sqler.ScriptService;
import com.sinotrans.ams.common.utils.StringUtils;

@RestController
@Transactional
public class AssetsService {

	@Autowired
	ScriptService scriptService;

	/**
	 * 资产录入
	 * @param params
	 * @return
	 */
	@RequestMapping("/add")
	public ResponseResult addService(@RequestBody JSONObject params){
		
		if(StringUtils.isEmpty(params.getString(""))){
			return new ResponseError(10001,"某某数据不能为空");
		}
		
		scriptService.execute("zcadd",params);
		return new ResponseSuccess(null);
	}
	
	/**
	 * 资产修改
	 * @param params
	 * @return
	 */
	@RequestMapping("/update")
	public ResponseResult updateService(@RequestBody JSONObject params){
		return new ResponseSuccess(null);
	}
	
	
	
}
