package com.sinotrans.ams.files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.message.ResponseResult;
import com.sinotrans.ams.common.message.ResponseSuccess;
import com.sinotrans.ams.common.upload.UploadFile;
import com.sinotrans.ams.common.upload.UploadFileHandle;

@CrossOrigin
@RefreshScope
@RestController
public class UploadFilesAction {
	
	@Autowired
	UploadFileHandle uploadFileHandle;
	
	@Autowired
	DaoUtil daoUtil;

	@Autowired
	UploadFileConfig uploadFileConfig;
	
	static Map<String,String> contentTypeMap = new HashMap<String,String>();
	
	static {
		contentTypeMap.put(".JPEG", "image/jpeg");
		contentTypeMap.put(".JPG", "image/jpeg");
		contentTypeMap.put(".JPE", "image/jpeg");
		contentTypeMap.put(".PNG", "image/png");
		contentTypeMap.put(".PDF", "application/pdf");
		contentTypeMap.put(".TIF", "image/tiff");
		contentTypeMap.put(".TIFF", "image/tiff");
		contentTypeMap.put(".GIF", "image/gif");
		contentTypeMap.put(".DOC", "application/msword");
		contentTypeMap.put(".DOCX", "application/msword");
		contentTypeMap.put(".PPT", "application/x-ppt");
		contentTypeMap.put(".PPTX", "application/x-ppt");
		contentTypeMap.put(".XLS","application/vnd.ms-excel");
		contentTypeMap.put(".XLSX","application/vnd.ms-excel");
	}
	
	final static String GET_FILE_SQL = "SELECT file_id AS `fileId` , file_name AS `name` , file_size AS `size` , created_date AS `uploadDate` , created_by AS `uploadBy`, business_type as `businessType`  FROM ams_fs_files where file_id = :fileId"; 
	
	@RequestMapping(value = "/upload/{businessType}" , method = RequestMethod.POST)
	public ResponseResult fileUploadService(
			@PathVariable(name="businessType",required = true) String businessType){
		Principal user = SecurityContextHolder.getContext().getAuthentication();
		return new ResponseSuccess(uploadFileHandle.uploadFile(businessType,user.getName()));
	}
	
	@RequestMapping(value = "/download/{fileId}" , method = RequestMethod.GET)
	public void downloadService(
			@PathVariable(name="fileId",required = true) String fileId) throws IOException{
		UploadFile uploadFile = getUploadFile(fileId);
		File file = getRealFile(uploadFile);
		download(file,uploadFile.getName(), uploadFile.getExtension());
	}

	private File getRealFile(UploadFile uploadFile) throws IOException {
		if(uploadFile == null){
			fileNotFound();
		}
		String dirPath = uploadFileConfig.getParentPath(uploadFile.getBusinessType(), uploadFile.getUploadDate());
		File file = new File(dirPath,uploadFile.getFileId());
		return file;
	}
	
	private void download(File file,String fileName,String extension) throws IOException {
		if(!file.exists()){
			fileNotFound();
			return;
		}
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		String contentType = contentTypeMap.get(extension.toUpperCase());;
		if(contentType == null){
			contentType = "application/octet-stream;charset=utf-8";
			response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			response.setCharacterEncoding("UTF-8");
		}
		response.setContentType(contentType);
		writeFile(response, file);
	}

	
	private void writeFile(HttpServletResponse response, File file) throws IOException {
		byte[] buff = Files.readAllBytes(file.toPath());
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		try{
			out.write(buff);
			out.flush();
		}finally{
			if(out !=null){
				out.close();
			}
		}
	}
	
	private void fileNotFound() throws IOException {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		PrintWriter writer = null;
		try{
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.setStatus(404);
			writer = response.getWriter();
			writer.write("文件不存在");
			writer.flush();
		}finally{
			if(writer!=null){
				writer.close();
			}
		}
	}

	private UploadFile getUploadFile(String fileId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fileId", fileId);
		UploadFile uploadFile = daoUtil.queryObject(GET_FILE_SQL , params , UploadFile.class);
		return uploadFile;
	}
	
	@RequestMapping(value = "/getFileInfo/{fileId}" , method = RequestMethod.POST)
	public ResponseResult getFileInfoService(
			@PathVariable(name="fileId",required = true) String fileId){
		UploadFile uploadFile = getUploadFile(fileId);
		return new ResponseSuccess(uploadFile);
	}
}
