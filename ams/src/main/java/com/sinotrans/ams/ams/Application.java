package com.sinotrans.ams.ams;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.sinotrans.ams.common.message.EnableFormatMessage;
import com.sinotrans.ams.common.sqler.EnableSqler;

@EnableResourceServer
@SpringBootApplication
@ComponentScan(basePackages={"com.sinotrans"})
@EnableSqler
@EnableFormatMessage
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).run(args);
	}
	
}
