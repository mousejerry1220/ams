package com.sinotrans.ams.common.upload;

import org.springframework.context.ApplicationEvent;

public class UploadFileEvent extends ApplicationEvent{

	public UploadFileEvent(UploadFile source) {
		super(source);
	}

	private static final long serialVersionUID = 1L;

}
