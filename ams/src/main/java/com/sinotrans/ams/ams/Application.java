package com.sinotrans.ams.ams;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import com.sinotrans.ams.common.message.EnableFormatMessage;
import com.sinotrans.ams.common.sqler.EnableSqler;


@SpringBootApplication
@ComponentScan(basePackages={"com.sinotrans"})
@EnableSqler
@EnableFormatMessage
public class Application {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).run(args);
	}
	
}
