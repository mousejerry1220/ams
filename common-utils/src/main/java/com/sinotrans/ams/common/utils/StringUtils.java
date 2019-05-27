package com.sinotrans.ams.common.utils;

import java.util.Map;
import java.util.UUID;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

public class StringUtils extends org.springframework.util.StringUtils{

	public static String processTemplate(String freemarkTemlate,Object paramObj){
		String templateName = UUID.randomUUID().toString();
		Configuration freemarkerTemplateConfiguration = new Configuration(Configuration.VERSION_2_3_28);
		StringTemplateLoader loader = new StringTemplateLoader();
		try {
			freemarkerTemplateConfiguration.setTemplateLoader(loader);
			loader.putTemplate(templateName, freemarkTemlate);
			if(paramObj instanceof Map){
				return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplateConfiguration.getTemplate(templateName), paramObj);
			}else{
				return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplateConfiguration.getTemplate(templateName), BeanMap.create(paramObj));
			}
		} catch (Exception e) {
			throw new RuntimeException("模板出错：" + freemarkTemlate + "exception: " + e.getMessage());
		} finally {
			loader.removeTemplate(templateName);
		}
	}
	
	public static String UUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
