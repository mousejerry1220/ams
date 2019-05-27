package com.sinotrans.ams.auth.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinotrans.ams.common.utils.MD5Util;

@RestController
@Service
public class AuthUserDetailsService implements UserDetailsService {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Map<String, Object> apiMap = namedParameterJdbcTemplate.queryForObject(
				" SELECT * FROM SCRIPT_DEF WHERE SCRIPT_CODE = 'auth' ", new MapSqlParameterSource(null),
				new ColumnMapRowMapper());
		if (apiMap == null) {
			throw new UsernameNotFoundException("登录SCRIPT无效的访问地址,请查看配置");
		}
		
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("username", username);
		Map<String, Object> userMap = null;
		try {
			String apiContent = null;
			if (apiMap.get("SCRIPT_CONTENT") instanceof byte[]) {
				apiContent = new String((byte[]) apiMap.get("SCRIPT_CONTENT"));
			} else {
				apiContent = (String) apiMap.get("SCRIPT_CONTENT");
			}
			userMap = namedParameterJdbcTemplate.queryForObject(apiContent, new MapSqlParameterSource(paramsMap),new ColumnMapRowMapper());
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException("无效的用户名或密码");
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(MD5Util.MD5Encode((String) userMap.get("PASSWORD"),"UTF-8"));
		
		Duty currentDuty = getcurrentDuty(username);
		List<Duty> dutyList = getDutyList(username);
		
		user.setCurrentDuty(currentDuty);
		user.setDutyList(dutyList);
		return user;
	}

	//TODO
	private List<Duty> getDutyList(String username) {
		List<Duty> list = new ArrayList<>();
		list.add(new Duty("1","职位一",new Ou("81","公司一"),new ArrayList<>()));
		return list;
	}

	private Duty getcurrentDuty(String username) {
		return new Duty("1","职位一",new Ou("81","公司一"),new ArrayList<>());
	}

	@RequestMapping("/user")
	public Object user() {
		Principal user = SecurityContextHolder.getContext().getAuthentication();
		return ((OAuth2Authentication) user).getPrincipal();
	}

}
