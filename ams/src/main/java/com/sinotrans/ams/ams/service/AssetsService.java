package com.sinotrans.ams.ams.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.message.ResponseResult;
import com.sinotrans.ams.common.message.ResponseSuccess;
import com.sinotrans.ams.common.sqler.ScriptService;

@CrossOrigin
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
	@RequestMapping("/asset/add")
	public ResponseResult addService(@RequestBody JSONObject params){
		
		//TODO 验证传入的参数合法性的代码
		
		//TODO 处理数据的逻辑的代码
		params.put("ASSET_ID", 1);
		//插入数据库
		scriptService.execute("ams_asset_book_add",params);
		scriptService.execute("ams_asset_card_add",params);
		scriptService.execute("ams_asset_distribution_add",params);
		scriptService.execute("ams_asset_other",params);
		batchExecute(params,"distributionList","");
		batchExecute(params,"attachmentList","");
		batchExecute(params,"invoceList","");
		
		return new ResponseSuccess(params);
	}
	
	public void batchExecute(JSONObject params,String keys,String scriptName){
		JSONArray list = params.getJSONArray(keys);
		for(Object item : list){
			scriptService.execute(scriptName,(JSONObject)item);
		}
	}

	/**
	 * 资产修改
	 * @param params
	 * @return
	 */
	@RequestMapping("/asset/update")
	public ResponseResult updateService(@RequestBody JSONObject params){
		return new ResponseSuccess(null);
	}
	
}
