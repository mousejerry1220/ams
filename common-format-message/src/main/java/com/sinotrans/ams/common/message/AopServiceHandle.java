package com.sinotrans.ams.common.message;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

@Aspect
public class AopServiceHandle {

	@Autowired
	InputMessageHandle inputMessageHandle;

	@Before("execution(* com.sinotrans..*.*Service(..))")
	public void paramsHandle(JoinPoint point) throws Throwable {
		if (point.getArgs().length > 0) {
			if (point.getArgs()[0] instanceof JSONObject) {
				JSONObject json = (JSONObject) point.getArgs()[0];
				inputMessageHandle.handle(json);
			}
		}
	}
}
