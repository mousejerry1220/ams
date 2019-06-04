package com.sinotrans.ams.common.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.sinotrans.ams.common.utils.StringUtils;

public class UploadFileHandle {

	@Autowired
    ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	StandardServletMultipartResolver standardServletMultipartResolver;
	
	public List<UploadFile> uploadFile(String businessType , String uploadBy) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		if(!standardServletMultipartResolver.isMultipart(request)){
			throw new RuntimeException("上传文件参数有错误");
		}
		
		MultipartHttpServletRequest multipartRequest = standardServletMultipartResolver.resolveMultipart(request);
		
		Map<String, List<MultipartFile>> map = multipartRequest.getMultiFileMap();
		Set<String> keys = map.keySet();
		Iterator<String> it = keys.iterator();
		List<UploadFile> result = new ArrayList<UploadFile>();
		while (it.hasNext()) {
			String filedName = it.next();
			List<MultipartFile> list = (List<MultipartFile>) map.get(filedName);
			for (MultipartFile file : list) {
				if(file.getSize() > 0){
					UploadFile uf = createUploadFile(file,businessType,uploadBy);
					applicationEventPublisher.publishEvent(new UploadFileEvent(uf));
					result.add(uf);
				}
			}
		}
		return result;
	}

	private UploadFile createUploadFile(MultipartFile file,String businessType,String uploadBy) {
		UploadFile uf = new UploadFile();
		uf.setBusinessType(businessType);
		uf.setUploadBy(uploadBy);
		uf.setFileId(StringUtils.UUID());
		String name = file.getOriginalFilename();
		uf.setName(name);
		try {
			uf.setBody(file.getBytes());
		} catch (IOException e) {
			//忽略
		}
		uf.setSize(file.getSize());
		return uf;
	}
	
	
	public UploadFile upload(String name,String base64) throws IOException {
		return upload(name, null, base64);
	}
	
	//上传base64编码格式的文件
	public UploadFile upload(String name,String keyId,String base64) throws IOException {
		if(base64 == null){
			throw new IllegalArgumentException("图片不能为空");
		}
		if(StringUtils.isEmpty(keyId)){
			keyId = StringUtils.UUID();
		}
		byte[] bytes = base64.getBytes();
		byte[] imageBytes = Base64.decodeBase64(bytes);
		UploadFile up = new UploadFile();
		int len = imageBytes.length;
		up.setBody(imageBytes);
		up.setFileId(keyId);
		up.setSize(new Long(len));
		up.setName(name);
		up.setBody(imageBytes);
		applicationEventPublisher.publishEvent(up);
		return up;
	}

}
