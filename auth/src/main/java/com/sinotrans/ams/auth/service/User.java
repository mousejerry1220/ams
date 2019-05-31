package com.sinotrans.ams.auth.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails{

	private static final long serialVersionUID = 1L;

	private String password;
	
	private String username;
	
	private List<GrantedAuthority> authorityList;
	
	private String duty;
	
	private String language;
	
	//业务字段
	private Duty currentDuty;

	private List<Duty> dutyList;
	
	public List<GrantedAuthority> getAuthorityList() {
		return authorityList;
	}

	public void setAuthorityList(List<GrantedAuthority> authorityList) {
		this.authorityList = authorityList;
	}

	public Duty getCurrentDuty() {
		return currentDuty;
	}

	public void setCurrentDuty(Duty currentDuty) {
		this.currentDuty = currentDuty;
	}

	public List<Duty> getDutyList() {
		return dutyList;
	}

	public void setDutyList(List<Duty> dutyList) {
		this.dutyList = dutyList;
	}

	public void setAuthorities(List<GrantedAuthority> authorityList) {
		this.authorityList = authorityList;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		if(authorityList == null){
			authorityList = new ArrayList<>();
		}
		return authorityList;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	//setters
	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}
