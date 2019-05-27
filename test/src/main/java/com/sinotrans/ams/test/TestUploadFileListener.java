package com.sinotrans.ams.test;

import org.springframework.context.ApplicationListener;

import com.sinotrans.ams.common.upload.UploadFileEvent;


public class TestUploadFileListener implements ApplicationListener<UploadFileEvent>{

	@Override
	public void onApplicationEvent(UploadFileEvent uploadFileEvent) {
		System.out.println(uploadFileEvent.getSource());
	}

}
