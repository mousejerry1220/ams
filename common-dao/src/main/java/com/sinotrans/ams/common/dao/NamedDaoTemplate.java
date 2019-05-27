package com.sinotrans.ams.common.dao;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.sinotrans.ams.common.utils.Page;

/**
 * 
 * @author Jerry.Zhao
 * 具名DAO，不建议直接使用该类，请通过{@link org.xsnake.cloud.dao.DaoUtil}调用
 *
 */
@Configuration
public class NamedDaoTemplate {
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	Dialect dialect;
	
	public int[] batchUpdate(String sql,Map<String, ?>[] batchValues){
		return namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
	}
	
	public int[] batchUpdate(String sql,List<Map<String,?>> batchValues){
		@SuppressWarnings("unchecked")
		Map<String,?>[] map = (Map<String,?>[])batchValues.toArray();
		return namedParameterJdbcTemplate.batchUpdate(sql, map);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int update(String sql,Object param){
		SqlParameterSource sqlParameterSource = null;
		if(param instanceof Map){
			sqlParameterSource = new MapSqlParameterSource((Map)param);
		}else{
			sqlParameterSource = new BeanPropertySqlParameterSource(param);
		}
		return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T queryForObject(String sql,Object param,Class<T> requiredType){
		RowMapper<T> rowMapper = null;
		SqlParameterSource sqlParameterSource = null;
		if(isSingleColumnClass(requiredType)){
			rowMapper = new SingleColumnRowMapper<T>(requiredType);
		}else{
			rowMapper = new BeanPropertyRowMapper<T>(requiredType);
		}
		
		if(param == null){
			param = new HashMap<String,Object>();
		}
		
		if(param instanceof Map){
			sqlParameterSource = new MapSqlParameterSource((Map)param);
		}else{
			sqlParameterSource = new BeanPropertySqlParameterSource(param);
		}
		try{
			return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> queryForObject(String sql,Object param){
		if(param == null){
			param = new HashMap<String,Object>();
		}
		SqlParameterSource sqlParameterSource = null;
		if(param instanceof Map){
			sqlParameterSource = new MapSqlParameterSource((Map)param);
		}else{
			sqlParameterSource = new BeanPropertySqlParameterSource(param);
		}
		
		try{
			return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource,new ColumnMapRowMapper());
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> queryForList(String sql, Object param,Class<T> requiredType){
		RowMapper<T> rowMapper = null;
		if(isSingleColumnClass(requiredType)){
			rowMapper = new SingleColumnRowMapper<T>(requiredType);
		}else{
			rowMapper = new BeanPropertyRowMapper<T>(requiredType);
		}
		
		if(param == null){
			param = new HashMap<String,Object>();
		}
		
		SqlParameterSource sqlParameterSource = null;
		if(param instanceof Map){
			sqlParameterSource = new MapSqlParameterSource((Map)param);
		}else{
			sqlParameterSource = new BeanPropertySqlParameterSource(param);
		}
		return namedParameterJdbcTemplate.query(sql, sqlParameterSource,rowMapper);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> queryForList(String sql, Object param){
		if(param == null){
			param = new HashMap<String,Object>();
		}
		SqlParameterSource sqlParameterSource = null;
		if(param instanceof Map){
			sqlParameterSource = new MapSqlParameterSource((Map)param);
		}else{
			sqlParameterSource = new BeanPropertySqlParameterSource(param);
		}
		return namedParameterJdbcTemplate.query(sql, sqlParameterSource,new ColumnMapRowMapper());
	}
	
	private String getPageSQL(String sql, int start, int end){
		return dialect.getPageSql(sql, start, end + start);
	}
	
	
	public Page<Map<String,Object>> queryForPage(String sql, Object param, int page ,int rows) {
		if(param == null){
			param = new HashMap<String,Object>();
		}
		try {
			String thql = "select count(1) from (" + sql +") t ";
			BigDecimal _count = queryForObject(thql, param, BigDecimal.class);
			int count = _count.intValue();
			List<Map<String,Object>> results = queryForList(getPageSQL(sql, (page - 1) * rows, rows ) ,param);
			return new Page<Map<String,Object>>(results, page, rows, count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> Page<T> queryForPage(String sql, Object param, int page ,int rows,Class<T> clazz) {
		if(param == null){
			param = new HashMap<String,Object>();
		}
		try {
			String thql = "select count(1) from (" + sql +") t ";
			BigDecimal _count = queryForObject(thql, param, BigDecimal.class);
			int count = _count.intValue();
			List<T> results = queryForList(getPageSQL(sql, (page - 1) * rows, rows ) ,param,clazz);
			return new Page<T>(results, page, rows, count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isSingleColumnClass(Class<?> requiredType){
		if( String.class == requiredType || 
		boolean.class == requiredType ||
		Boolean.class == requiredType ||
		byte.class == requiredType || 
		Byte.class == requiredType ||
		short.class == requiredType || 
		Short.class == requiredType||
		int.class == requiredType || 
		Integer.class == requiredType ||
		long.class == requiredType || 
		Long.class == requiredType||
		float.class == requiredType || 
		Float.class == requiredType||
		double.class == requiredType || 
		Double.class == requiredType || 
		Number.class == requiredType||
		BigDecimal.class == requiredType ||
		java.sql.Date.class == requiredType||
		java.sql.Time.class == requiredType||
		java.sql.Timestamp.class == requiredType || 
		java.util.Date.class == requiredType||
		byte[].class == requiredType||
		Blob.class == requiredType||
		Clob.class == requiredType )
			return true;
		return false;
	}

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public Dialect getDialect() {
		return dialect;
	}

}
