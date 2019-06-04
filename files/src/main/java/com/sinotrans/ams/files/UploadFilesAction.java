package com.sinotrans.ams.files;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sinotrans.ams.common.message.ResponseResult;
import com.sinotrans.ams.common.message.ResponseSuccess;
import com.sinotrans.ams.common.upload.UploadFileHandle;

@RefreshScope
@RestController
public class UploadFilesAction {
	
	@Autowired
	UploadFileHandle uploadFileHandle;
	
	@RequestMapping(value = "/uploadFile/{businessType}" , method = RequestMethod.POST)
	public ResponseResult fileUploadService(
			@PathVariable(name="businessType",required = true) String businessType){
		
		Principal user = SecurityContextHolder.getContext().getAuthentication();
		
		return new ResponseSuccess(uploadFileHandle.uploadFile(businessType,user.getName()));
	}
	
}
