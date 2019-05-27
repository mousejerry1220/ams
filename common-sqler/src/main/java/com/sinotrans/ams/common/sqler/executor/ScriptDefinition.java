package com.sinotrans.ams.common.sqler.executor;

import java.io.Serializable;

public class ScriptDefinition implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String scriptCode;
	private String scriptContent;
	private String scriptType;
	
	private Long updateTimestamp;

	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	public String getScriptContent() {
		return scriptContent;
	}

	public void setScriptContent(String scriptContent) {
		this.scriptContent = scriptContent;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public Long getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Long updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

}
