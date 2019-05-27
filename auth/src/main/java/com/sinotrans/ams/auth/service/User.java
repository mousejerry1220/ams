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
	
}
