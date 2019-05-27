package com.sinotrans.ams.task;

import java.util.Date;

public class DataTask {

	private String serviceCode;
	
	private Date lastUpdateDate;
	
	private String cron;
	
	private String updateSql;
	
	private String insertSql;
	
	public boolean isOK(){
		if(serviceCode != null 
				&& cron != null
				&& updateSql != null
				&& insertSql != null
				){
			if(lastUpdateDate == null){
				lastUpdateDate = new Date(0);
			}
			return true;
		} 
		return false;
	}
	
	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
}
