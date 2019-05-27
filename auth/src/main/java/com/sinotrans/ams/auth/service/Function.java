package com.sinotrans.ams.auth.service;

import java.io.Serializable;

public class Function implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String index;

	public Function(String index) {
		this.index = index;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
