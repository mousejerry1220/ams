package com.sinotrans.ams.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sinotrans.ams.auth.service.AuthUserDetailsService;
import com.sinotrans.ams.common.utils.MD5Util;

/**
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthUserDetailsService authUserDetailsService;
	
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new PasswordEncoder() {
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				if (encodedPassword == null || encodedPassword.length() == 0) {
					return false;
				}
				return encodedPassword.equals(MD5Util.MD5Encode(rawPassword.toString(),"UTF-8"));
			}
			
			@Override
			public String encode(CharSequence rawPassword) {
				return MD5Util.MD5Encode(rawPassword.toString(), "UTF-8");
			}
		};
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        	.antMatchers("/*").permitAll()
          .and()
          	.cors().disable()
            .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
