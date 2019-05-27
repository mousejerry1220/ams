package com.sinotrans.ams.auth.service;

import java.io.Serializable;
import java.util.List;

public class Duty implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;

	private Ou ou;
	
	private List<Function> functionList;
	
	public Duty(){}
	
	public Duty(String id, String name,Ou ou,List<Function> functionList) {
		this.id = id;
		this.name = name;
		this.ou = ou;
		this.functionList = functionList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Ou getOu() {
		return ou;
	}

	public void setOu(Ou ou) {
		this.ou = ou;
	}

	public List<Function> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<Function> functionList) {
		this.functionList = functionList;
	}
	
}
