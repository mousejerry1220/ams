package com.sinotrans.ams.common.upload;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages={"com.sinotrans"})
@EnableFileUpload
public class Application {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).run(args);
	}
	
}
