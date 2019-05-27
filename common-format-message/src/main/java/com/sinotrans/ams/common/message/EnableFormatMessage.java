package com.sinotrans.ams.common.message;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({FormatMessageRegistrar.class,InputMessageHandle.class,AopServiceHandle.class,JsonMessageConverter.class})
public @interface EnableFormatMessage {
	
	
}
