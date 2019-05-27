package com.sinotrans.ams.common.sqler.executor.impl;

import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.sqler.ApplicationContextProvider;
import com.sinotrans.ams.common.sqler.executor.APIExecutor;
import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;
import com.sinotrans.ams.common.sqler.executor.ExecuteContext;
/**
 * 查询单个对象
 * @author Jerry.Zhao
 *
 */
public class SearchObjectExecutor extends APIExecutor {

	public SearchObjectExecutor(ScriptDefinition daoDef) {
		super(daoDef);
	}

	@Override
	public Object execute(String script,ExecuteContext context) {
		DaoUtil daoUtil = ApplicationContextProvider.getDaoUtil();
		return daoUtil.queryObject(script,context.getParams());
	}

}
