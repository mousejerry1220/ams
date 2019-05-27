package com.sinotrans.ams.common.sqler;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.sqler.executor.APIExecutor;
import com.sinotrans.ams.common.sqler.executor.ExecuteContext;
import com.sinotrans.ams.common.sqler.executor.ExecutorFactory;
import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;
import com.sinotrans.ams.common.sqler.executor.log.InvokeLogger;
import com.sinotrans.ams.common.sqler.executor.log.InvokeLoggerEvent;
import com.sinotrans.ams.common.utils.ExceptionUtils;


@Component
public class ScriptService {

	@Autowired
	DaoUtil daoUtil;
	
	@Autowired
    ApplicationEventPublisher applicationEventPublisher;
	
	final static String API_SQL = " SELECT SCRIPT_CODE, SCRIPT_CONTENT,SCRIPT_TYPE FROM SCRIPT_DEF WHERE SCRIPT_CODE = :SCRIPT_CODE "; 

	public Object execute(String scriptCode) {
		return execute(scriptCode, null);
	}
	
	public Object execute(String scriptCode, JSONObject params) {
		if(params == null){
			params = new JSONObject();
		}
		//查询API定义内容
		Map<String,Object> args = new HashMap<String,Object>();
		args.put("SCRIPT_CODE", scriptCode);
		ScriptDefinition apiDef = daoUtil.queryObject(API_SQL,args,ScriptDefinition.class);
		if(apiDef == null){
			throw new IllegalArgumentException("无效的访问。请确定/" + scriptCode + "脚本服务是否存在");
		}
		putRequestParams2Params(params);
		return executeService(params, apiDef);
	}

	//创建脚本执行器，并执行脚本
	private Object executeService(JSONObject params, ScriptDefinition apiDef) {
		long start = System.currentTimeMillis();
		APIExecutor executor = ExecutorFactory.factory(apiDef);
		String error = null;
		try{
			return executor.exec(new ExecuteContext(params,this));
		}catch (Exception e){
			error = ExceptionUtils.getExceptionStackTrace(e);
			throw new RuntimeException(e.getMessage());
		}finally{
			long expend = System.currentTimeMillis() - start; 
//			if(expend > 2000 || error != null){
				logger(apiDef,params, expend , error);
//			}
		}
	}

	//将request中的参数加入到params request不为空说明为主函数执行
	private void putRequestParams2Params(JSONObject params) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String name = names.nextElement();
			params.put(name, request.getParameter(name));
		}
	}
	
	private void logger(ScriptDefinition daoDef, JSONObject params,  long expend , String error ) {
		InvokeLogger logger = new InvokeLogger(daoDef, JSON.toJSONString(params) , expend, error);
		applicationEventPublisher.publishEvent(new InvokeLoggerEvent(logger));
	}

}
