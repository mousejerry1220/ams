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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.context.SecurityContextHolder;
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

@RefreshScope
@RestController
public class UploadFilesAction {
	
	@Autowired
	UploadFileHandle uploadFileHandle;
	
	@Autowired
	DaoUtil daoUtil;

	@Autowired
	UploadFileConfig uploadFileConfig;
	
	final static String GET_FILE_SQL = "SELECT file_id AS `fileId` , file_name AS `name` , file_size AS `size` , created_date AS `uploadDate` , created_by AS `uploadBy`  FROM ams_fs_files where file_id = : fileId"; 
	
	@RequestMapping(value = "/upload/{businessType}" , method = RequestMethod.POST)
	public ResponseResult fileUploadService(
			@PathVariable(name="businessType",required = true) String businessType){
		Principal user = SecurityContextHolder.getContext().getAuthentication();
		return new ResponseSuccess(uploadFileHandle.uploadFile(businessType,user.getName()));
	}
	
	@RequestMapping(value = "/download/{fileId}" , method = RequestMethod.POST)
	public void downloadService(
			@PathVariable(name="fileId",required = true) String fileId) throws IOException{
		UploadFile uploadFile = getUploadFile(fileId);
		File file = getRealFile(uploadFile);
		download(file, uploadFile.getName());
	}

	private File getRealFile(UploadFile uploadFile) throws IOException {
		if(uploadFile == null){
			fileNotFound();
		}
		String dirPath = uploadFileConfig.getParentPath(uploadFile.getBusinessType(), uploadFile.getUploadDate());
		File file = new File(dirPath,uploadFile.getFileId());
		return file;
	}
	
	@RequestMapping("/image/{fileId}")
	public String image(@PathVariable(name="fileId",required = true) String fileId) throws IOException {
		UploadFile uploadFile = getUploadFile(fileId);
		File file = getRealFile(uploadFile);
		image(file,uploadFile.getExtension());
		return null;
	}
	
	private String image(File file,String extension) throws IOException {
		if(!file.exists()){
			fileNotFound();
			return null;
		}
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		String imageType = "image/jpeg";
		if(extension.toUpperCase().endsWith(".JPEG") || extension.toUpperCase().endsWith(".JPG")){
			imageType = "image/jpeg";
		} else if(extension.toUpperCase().endsWith(".PNG")){
			imageType = "image/png";
		}
		response.setContentType(imageType);
		writeFile(response, file);
		return null;
	}
	
	private String download(File file,String fileName) throws IOException {
		if(!file.exists()){
			fileNotFound();
			return null;
		}
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		String name = new String(fileName.replaceAll(" ","_").getBytes("GBK"),"ISO8859-1");
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="+name);
		response.setCharacterEncoding("UTF-8");
		writeFile(response, file);
		return null;
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
