package com.sinotrans.ams.common.sqler.executor.log;

import org.springframework.context.ApplicationEvent;

public class InvokeLoggerEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	public InvokeLoggerEvent(InvokeLogger source) {
		super(source);
	}

}
