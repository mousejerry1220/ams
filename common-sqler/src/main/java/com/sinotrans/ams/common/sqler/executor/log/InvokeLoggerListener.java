package com.sinotrans.ams.common.sqler.executor.log;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sinotrans.ams.common.dao.DaoUtil;


@Component
public class InvokeLoggerListener implements ApplicationListener<InvokeLoggerEvent> {

	@Autowired
	DaoUtil daoUtil;
	
	Logger logger = Logger.getLogger(InvokeLoggerListener.class);
	
	@Override
	public void onApplicationEvent(InvokeLoggerEvent event) {
		InvokeLogger invokeLogger = (InvokeLogger)event.getSource();
		daoUtil.update("insert into SCRIPT_IVK_LOG (CREATE_DATE,SCRIPT_CODE,EXPEND_TIME,PARAMS,ERROR,SCRIPT_CONTENT) values (:createDate, :scriptCode, :expendTime, :params, :error, :scriptContent) ", invokeLogger);
		String log =  "/" + invokeLogger.scriptCode + " 调用耗时：" + invokeLogger.expendTime + " 参数 ：" + JSONObject.toJSONString(invokeLogger.params);
		logger.info(log);
	}
	
}