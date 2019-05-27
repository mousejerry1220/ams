package com.sinotrans.ams.common.sqler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import com.sinotrans.ams.common.dao.DaoUtil;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{

    private static ApplicationContext applicationContext;
    
    @Autowired
    DaoUtil daoUtil;
    
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

	public static DaoUtil getDaoUtil() {
		return applicationContext.getBean(DaoUtil.class);
	}

	public static DataSourceTransactionManager getDataSourceTransactionManager() {
		return applicationContext.getBean(DataSourceTransactionManager.class);
	}
    
    
}