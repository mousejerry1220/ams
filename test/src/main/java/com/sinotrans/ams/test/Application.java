package com.sinotrans.ams.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

import com.sinotrans.ams.common.message.EnableFormatMessage;
import com.sinotrans.ams.common.sqler.EnableSqler;
import com.sinotrans.ams.common.upload.EnableFileUpload;

@RefreshScope
@SpringBootApplication
@ComponentScan(basePackages={"com.sinotrans"})
//开启格式化消息
@EnableFormatMessage
//开启配置式脚本的数据库访问
@EnableSqler
//开启上传服务
@EnableFileUpload
//连接注册中心
@EnableDiscoveryClient
//开启熔断
@EnableHystrix
public class Application {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).run(args);
	}
	
}
