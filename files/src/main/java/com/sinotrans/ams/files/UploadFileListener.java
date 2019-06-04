package com.sinotrans.ams.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.upload.UploadFile;
import com.sinotrans.ams.common.upload.UploadFileEvent;

@Configuration
public class UploadFileListener implements ApplicationListener<UploadFileEvent>{

	public UploadFileListener(){
		System.out.println("=========Listener初始化========");
	}
	
	@Autowired
	DaoUtil daoUtil;
	
	@Autowired
	UploadFileConfig uploadFileConfig;
	
	@Override
	public void onApplicationEvent(UploadFileEvent uploadFileEvent) {
		UploadFile uploadFile = (UploadFile)uploadFileEvent.getSource();
		byte[] fileBody = uploadFile.getBody();
		uploadFile.setBody(null);
		uploadFile.setUploadDate(new Date());
		String dirPath = getFileDirPath(uploadFile.getBusinessType());
		File dirFile = new File(dirPath);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		File file = new File(dirFile, uploadFile.getFileId());
		try {
			Files.write(file.toPath(), fileBody);
		} catch (IOException e) {
//			这里可能因为磁盘存储不足导致失败，暂时忽略
		}
		daoUtil.update("INSERT INTO ams_fs_files (file_id,file_name,file_size,created_date,created_by,business_type,extension) values (:fileId, :name, :size, :uploadDate, :uploadBy, :businessType, :extension)", uploadFile);
	}

	private String getFileDirPath(String businessTye) {
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		return uploadFileConfig.getServerPath() + File.separator  + businessTye +  File.separator + date;
	}
	
}
