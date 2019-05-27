package com.sinotrans.ams.common.sqler.executor.impl;

import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.sqler.ApplicationContextProvider;
import com.sinotrans.ams.common.sqler.executor.APIExecutor;
import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;
import com.sinotrans.ams.common.sqler.executor.ExecuteContext;

public class SearchPageExecutor extends APIExecutor{

	public SearchPageExecutor(ScriptDefinition daoDef) {
		super(daoDef);
	}

	@Override
	public Object execute(String script,ExecuteContext context) {
		DaoUtil daoUtil = ApplicationContextProvider.getDaoUtil();
		Integer page = getValue(context.getParams(),"page",Integer.class);
		Integer rows = getValue(context.getParams(),"rows",Integer.class);
		page = page == null ? 1 : page;
		rows = rows == null ? 10 : rows;
		return daoUtil.queryPage(script, context.getParams(), page, rows);
	}
	

}
