package com.sinotrans.ams.common.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;


public class DaoCall {

	JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Object> executeCall(final String sql, final ProcedureParam[] args) {
		CallableStatementCallback<List<Object>> action = new CallableStatementCallback<List<Object>>() {
			@Override
			public List<Object> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				final List<Object> result = new ArrayList<Object>();
				for(int i=1;i<args.length+1;i++){
					ProcedureParam p = args[(i-1)];
					if(p instanceof InProcedureParam){
						cs.setObject(i, p.value);
					}else if(p instanceof OutProcedureParam){
						cs.registerOutParameter(i, p.type);
					}
				}
				cs.execute();
				for(int i=1;i<args.length+1;i++){
					ProcedureParam p = args[(i-1)];
					if(p instanceof OutProcedureParam){
						result.add(cs.getString(i));
					}else{
						result.add(null);
					}
				}
				return result;
			}
		};
		return jdbcTemplate.execute(sql,action);
	}
	
	public static abstract class ProcedureParam{
		public ProcedureParam (int type,Object value){
			this.type = type;
			this.value = value;
		}
		int type;
		Object value;
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	public static class InProcedureParam extends ProcedureParam{
		public InProcedureParam(Object value){
			super(-1,value);
		}
	}
	
	public static class OutProcedureParam extends ProcedureParam{
		public OutProcedureParam(int type){
			super(type,null);
		}
	}
	
}
