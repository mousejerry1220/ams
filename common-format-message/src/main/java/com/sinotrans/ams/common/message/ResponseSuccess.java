package com.sinotrans.ams.common.message;

import java.io.Serializable;

public class ResponseSuccess implements ResponseResult,Serializable{

	private static final long serialVersionUID = 1L;
	
	protected Object result = null;

	public ResponseSuccess(Object result){
		this.result = result;
	}
	
	public ResponseSuccess(){}

	public Object getResult() {
		return result;
	}
	
}
