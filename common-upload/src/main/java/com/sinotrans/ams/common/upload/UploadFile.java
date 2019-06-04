package com.sinotrans.ams.common.upload;

import java.io.Serializable;
import java.util.Date;

public class UploadFile implements Serializable{

	private static final long serialVersionUID = 1L;

	private byte[] body;

	private String name;

	private String fileId;

	private Long size;
	
	private String businessType;
	
	private String uploadBy;
	
	private Date uploadDate;
	
	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getExtension() {
		int lastPointIndex = name.lastIndexOf(".");
		return (name == null ? "" : (lastPointIndex > 0 ? name.substring(lastPointIndex) : ""));
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getUploadBy() {
		return uploadBy;
	}

	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
