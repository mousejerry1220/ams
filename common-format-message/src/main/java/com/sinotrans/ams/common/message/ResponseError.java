package com.sinotrans.ams.common.message;

import java.io.Serializable;

public class ResponseError implements ResponseResult,Serializable {

	private static final long serialVersionUID = 1L;
	
	int code = 0;
	
	String message = null;
	
	public ResponseError(int code,String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
