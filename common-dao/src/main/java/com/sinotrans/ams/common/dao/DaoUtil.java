package com.sinotrans.ams.common.dao;

import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.sinotrans.ams.common.utils.Page;

/**
 * 
 * @author Jerry.Zhao
 * 具名DAO查询常用类，本类中都为具名方式查询
 * 封装的DAO的最上层操作,主要分为两大类方式，SQL模板查询，以及一般SQL查询，
 * SQL模板查询：SQL模板可以是一个FreeMarker语法支持的模板，首先通过FreeMarker解析，然后将解析后的内容作为SQL语句进行查询
 *              所有的模板查询都以$符号开始的方法表示
 * 
 * 支持两种返回值:Bean返回值与Map返回值
 * 查询分为:1、单个对象或者数值查询，queryObject
 *         2、列表对象查询  queryList
 *         3、分页对象查询 queryPage
 * 
 */
@Configuration
public class DaoUtil {
	
	Logger log = Logger.getLogger(DaoUtil.class);
	
	@Autowired
	NamedDaoTemplate namedDaoTemplate;
	
	public NamedDaoTemplate getNamedDaoTemplate() {
		return namedDaoTemplate;
	}

	public void setNamedDaoTemplate(NamedDaoTemplate namedDaoTemplate) {
		this.namedDaoTemplate = namedDaoTemplate;
	}

	public int[] batchUpdate(String sql,Map<String, ?>[] batchValues){
		log.debug(sql);
		return namedDaoTemplate.batchUpdate(sql,batchValues);
	}
	
	public int[] batchUpdate(String sql,List<Map<String,?>> batchValues){
		log.debug(sql);
		return namedDaoTemplate.batchUpdate(sql,batchValues);
	}
	
	//sql+类返回
	public <T> T queryObject(String sql,Object paramObj,Class<T> requiredType) {
		return namedDaoTemplate.queryForObject(sql,paramObj,requiredType);
	}
	
	public <T> List<T> queryList(String sql,Object paramObj,Class<T> requiredType) {
		return namedDaoTemplate.queryForList(sql,paramObj,requiredType);
	}
	
	public <T> Page<T> queryPage(String sql,Object paramObj, int page ,int rows , Class<T> requiredType){
		return namedDaoTemplate.queryForPage(sql,paramObj,page , rows,requiredType);
	}

	public int update(String sql,Object paramObj) {
		return namedDaoTemplate.update(sql, paramObj);
	}
	
	//SQL+Map返回
	public Map<String,Object> queryObject(String sql,Object paramObj) {
		return namedDaoTemplate.queryForObject(sql,paramObj);
	}
	
	public List<Map<String,Object>> queryList(String sql,Object paramObj) {
		return namedDaoTemplate.queryForList(sql,paramObj);
	}
	
	public Page<Map<String,Object>> queryPage(String sql,Object paramObj, int page ,int rows ){
		return namedDaoTemplate.queryForPage(sql,paramObj,page , rows);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	//sql+类返回
	public <T> T queryObject(String sql,Class<T> requiredType) {
		return namedDaoTemplate.queryForObject(sql,null,requiredType);
	}
	
	public <T> List<T> queryList(String sql,Class<T> requiredType) {
		return namedDaoTemplate.queryForList(sql,null,requiredType);
	}
	
	public <T> Page<T> queryPage(String sql, int page ,int rows , Class<T> requiredType){
		return namedDaoTemplate.queryForPage(sql,null,page , rows,requiredType);
	}
	
	//SQL+Map返回
	public Map<String,Object> queryObject(String sql) {
		return namedDaoTemplate.queryForObject(sql,null);
	}
	
	public List<Map<String,Object>> queryList(String sql) {
		return namedDaoTemplate.queryForList(sql,null);
	}
	
	public Page<Map<String,Object>> queryPage(String sql, int page ,int rows ){
		return namedDaoTemplate.queryForPage(sql,null,page , rows);
	}
	
}
