package com.sinotrans.ams.common.upload;

import java.io.Serializable;

public class UploadFile implements Serializable{

	private static final long serialVersionUID = 1L;

	private byte[] body;

	private String name;

	private String keyId;

	private Long size;

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
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
