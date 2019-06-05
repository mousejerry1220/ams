package org.springframework.jdbc.core.namedparam;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NamedParameterUtil {
	
	public static List<String> getParameterNameList(String sql){
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
		return parsedSql.getParameterNames();
	}
	
	public static Set<String> getParameterNameSet(String sql){
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
		return new LinkedHashSet<>(parsedSql.getParameterNames());
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getParameterMap(String sql, Object param){
		Map<String,Object> m = new HashMap<String,Object>();
		Set<String> names = getParameterNameSet(sql);
		SqlParameterSource sqlParameterSource = null;
		
		if(param instanceof Map){
			sqlParameterSource = new MapSqlParameterSource((Map<String, ?>)param);
		}else{
			sqlParameterSource = new BeanPropertySqlParameterSource(param);
		}
		
		for(String name : names){
			m.put(name, sqlParameterSource.getValue(name));
		}
		return m;
	}
	
	public static void main(String[] args) {
		Map<String,Object> a = getParameterMap(" :b :a :a :b ",new HashMap<>());
		System.out.println(a);
	}
	
}
