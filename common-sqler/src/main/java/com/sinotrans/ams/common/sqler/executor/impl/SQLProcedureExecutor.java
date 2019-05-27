package com.sinotrans.ams.common.sqler.executor.impl;

import com.sinotrans.ams.common.sqler.executor.APIExecutor;
import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;
import com.sinotrans.ams.common.sqler.executor.ExecuteContext;

public class SQLProcedureExecutor extends APIExecutor{

	public SQLProcedureExecutor(ScriptDefinition daoDef) {
		super(daoDef);
	}

	@Override
	public Object execute(String script,ExecuteContext context) {
		return null;
	}

}
