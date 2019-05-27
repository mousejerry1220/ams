package com.sinotrans.ams.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.utils.CronExpression;
import com.sinotrans.ams.common.utils.ExceptionUtils;

@EnableScheduling
@Configuration
@Transactional(rollbackFor=Exception.class)
public class TaskScheduler {
	
	@Value("${data.synchronization.uri}")
	String dataSynchronizationURI;
	
	@Value("${data.synchronization.keyId}")
	String keyId;
	
	@Value("${data.synchronization.businessCode}")
	String businessCode;
	
	@Autowired
	DaoUtil daoUtil;
	
	Logger log = Logger.getLogger(TaskScheduler.class);
	
	List<DataTask> dataTaskList = null; 
	
	InvokeHttps invokeHttps = null;
	
	ExecutorService es = new ThreadPoolExecutor(1, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	private void init(){
		dataTaskList = daoUtil.queryList("select * from ams_setting_data_service ",DataTask.class);
	}
	
	@Scheduled(cron = "0 0/1 * * * ?")
	public void task() throws ParseException, DocumentException {
		init();
		Date now = new Date();
		for(DataTask task : dataTaskList){
			if(task.isOK()){
				try{
					CronExpression cronExpression = new CronExpression(task.getCron());
					if(cronExpression.isSatisfiedBy(now)){
						es.submit(new Runnable() {
							@Override
							public void run() {
								try {
									updateDatabase(task);
								} catch (DocumentException e) {
									logError(task, now, e);
									String error = ExceptionUtils.getExceptionStackTrace(e);
									log.error(error);
								}
							}
						});
					}
				}catch (ParseException e) {
					logError(task, now, e);
				}
				
			}
		}
	}
	
	
	private void updateDatabase(final DataTask task) throws DocumentException {
		log.info("执行Task -> " + task.getServiceCode());
		Date now = new Date();
		if(invokeHttps == null){
			invokeHttps = new InvokeHttps();
		}
		Date updateDate = task.getLastUpdateDate() == null ? now : task.getLastUpdateDate(); 
		task.setLastUpdateDate(updateDate);
		String parameter = task.getServiceCode() + "!" + new SimpleDateFormat("yyyyMMddHHmmss").format(updateDate);
		String url = dataSynchronizationURI + "?P_PARAMETER=" + parameter;
		String result = invokeHttps.doPost(url, keyId);
		task.setLastUpdateDate(now);
		daoUtil.update("update ams_setting_data_service set last_update_date = :lastUpdateDate where service_code = :serviceCode ", task);
		Document doc = DocumentHelper.parseText(result);
		Element root = doc.getRootElement();
		List<Element> lines = root.elements("LINE");
		@SuppressWarnings("unchecked")
		Map<String,?>[] datas = new HashMap[lines.size()];
		int index=0;
		for(Element line : lines){
			List<Element> fileds = line.elements();
			Map<String,Object> data = new HashMap<String,Object>();
			for(Element e : fileds){
				data.put(e.getName(), e.getText());
			}
			datas[index] = data;
			index++ ;
		}
		
		int[] results = null;
		try{
			results = daoUtil.batchUpdate(task.getUpdateSql(), datas);
		}catch (Exception e) {
			logError(task, now, e);
			throw e;
		}

		int count = 0;
		if(results.length == datas.length){
			for(int i : results){
				if(i == 0){
					count++;
				}
			}
		}
		
		//存在需要插入的行
		if(count > 0){
			@SuppressWarnings("unchecked")
			Map<String,?>[] insertDatas = new HashMap[count];
			for(int i=0;i<results.length;i++){
				if(results[i] == 0){
					insertDatas[i] = datas[i];
				}
			}
			
			try{
				daoUtil.batchUpdate(task.getInsertSql(), insertDatas);
			}catch (Exception e) {
				logError(task, now, e);
				throw e;
			}
			
		}
		
	}

	private void logError(DataTask task, Date now, Exception e) {
		String error = ExceptionUtils.getExceptionStackTrace(e);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("serviceCode", task.getServiceCode());
		map.put("executeDate", now);
		map.put("error", error);
		daoUtil.update("INSERT INTO ams_setting_data_service_log(service_code,execute_date,error_content) VALUES (:serviceCode,:executeDate,:error);", map);
	}
	
}
