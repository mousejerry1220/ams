package com.sinotrans.ams.common.sqler.executor.impl;

import java.util.List;
import java.util.Map;

import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.sqler.ApplicationContextProvider;
import com.sinotrans.ams.common.sqler.executor.APIExecutor;
import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;
import com.sinotrans.ams.common.sqler.executor.ExecuteContext;

public class SearchListExecutor extends APIExecutor{

	public SearchListExecutor(ScriptDefinition daoDef) {
		super(daoDef);
	}

	@Override
	public Object execute(String script,ExecuteContext context) {
		DaoUtil daoUtil = ApplicationContextProvider.getDaoUtil();
		List<Map<String, Object>> list = daoUtil.queryList(script,context.getParams());
		return list;
	}
	
	
	
}
