package com.sinotrans.ams.common.sqler.executor.impl;


import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.sqler.ApplicationContextProvider;
import com.sinotrans.ams.common.sqler.executor.APIExecutor;
import com.sinotrans.ams.common.sqler.executor.ScriptDefinition;
import com.sinotrans.ams.common.sqler.executor.ExecuteContext;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class GroovyScriptExecutor extends APIExecutor{

	public GroovyScriptExecutor(ScriptDefinition daoDef) {
		super(daoDef);
	}

	@Override
	public Object execute(String groovyScript,ExecuteContext context) {
		Binding binding = new Binding();
		DaoUtil daoUtil = (DaoUtil) ApplicationContextProvider.getDaoUtil();
		binding.setProperty("dao", daoUtil);
		binding.setProperty("params", context.getParams());
		GroovyShell shell = new GroovyShell(binding);
		Script script = shell.parse(groovyScript);
		return script.run();
	}

}
