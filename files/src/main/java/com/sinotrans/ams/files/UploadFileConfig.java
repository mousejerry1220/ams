package com.sinotrans.ams.files;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="upload")
public class UploadFileConfig {

	String serverPath;
	
	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	
	public String getParentPath(String businessType,Date date) {
		String dateDir = dateFormat.format(date);
		return serverPath + File.separator  + businessType +  File.separator + dateDir ; 
	}
	
}
