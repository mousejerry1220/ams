package com.sinotrans.ams.common.sqler.executor;

import com.sinotrans.ams.common.sqler.executor.impl.GroovyScriptExecutor;
import com.sinotrans.ams.common.sqler.executor.impl.SQLProcedureExecutor;
import com.sinotrans.ams.common.sqler.executor.impl.SearchLinkExecutor;
import com.sinotrans.ams.common.sqler.executor.impl.SearchListExecutor;
import com.sinotrans.ams.common.sqler.executor.impl.SearchObjectExecutor;
import com.sinotrans.ams.common.sqler.executor.impl.SearchPageExecutor;
import com.sinotrans.ams.common.sqler.executor.impl.UpdateSQLExecutor;

public class ExecutorFactory {

	public static APIExecutor factory(ScriptDefinition apiDef){
		if("SEARCH_OBJECT".equals(apiDef.getScriptType())){
			return new SearchObjectExecutor(apiDef);
		}
		
		if("SEARCH_LIST".equals(apiDef.getScriptType())){
			return new SearchListExecutor(apiDef);
		}
		
		if("SEARCH_LINK".equals(apiDef.getScriptType())){
			return new SearchLinkExecutor(apiDef);
		}
		
		if("SEARCH_PAGE".equals(apiDef.getScriptType())){
			return new SearchPageExecutor(apiDef);
		}
		
		if("GROOVY_SCRIPT".equals(apiDef.getScriptType())){
			return new GroovyScriptExecutor(apiDef);
		}
		
		if("UPDATE_SQL".equals(apiDef.getScriptType())){
			return new UpdateSQLExecutor(apiDef);
		}
		
		if("SQL_PROCEDURE".equals(apiDef.getScriptType())){
			return new SQLProcedureExecutor(apiDef);
		}
		
		throw new RuntimeException("无效的API配置,请确定API类型为SEARCH_OBJECT、SEARCH_LIST、SEARCH_PAGE、GROOVY_SCRIPT、SQL_PROCEDURE其中之一");
	}
	
}
