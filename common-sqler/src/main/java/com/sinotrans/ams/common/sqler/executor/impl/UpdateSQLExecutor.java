package com.sinotrans.ams.common.sqler.executor.impl;

import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.sqler.ApplicationContextProvider;
import com.sinotrans.ams.common.sqler.executor.APIExecutor;
import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;
import com.sinotrans.ams.common.sqler.executor.ExecuteContext;

public class UpdateSQLExecutor extends APIExecutor{

	public UpdateSQLExecutor(ScriptDefinition daoDef) {
		super(daoDef);
	}

	@Override
	public Object execute(String script, ExecuteContext context) {
		DaoUtil daoUtil = ApplicationContextProvider.getDaoUtil();
		return daoUtil.update(script, context.getParams());
	}
	
}
