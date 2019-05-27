package com.sinotrans.ams.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sinotrans.ams.common.dao.DaoUtil;
import com.sinotrans.ams.common.message.ResponseResult;
import com.sinotrans.ams.common.message.ResponseSuccess;
import com.sinotrans.ams.common.redis.RedisUtil;
import com.sinotrans.ams.common.sqler.ScriptService;
import com.sinotrans.ams.common.upload.UploadFileHandle;

@RefreshScope
@RestController
public class TestAction {

	@Autowired
	DaoUtil daoUtil;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	ScriptService scriptService;
	
	@Autowired
	UploadFileHandle uploadFileHandle;
	
	@Value("${test.value}")
	String testValue;
	
	/**
	 * 需要POM依赖common-format-message
	 * 需要注解@EnableFormatMessage
	 * @param params
	 * * 按照规范写的Service，会通过AOP解析外层包。
	 * 协议数据包格式为:
	 * {
	 * 		"messageId"    : "请求消息ID",  //消息唯一ID,服务端回复消息时会原样回复给客户端
	 * 		"params"       : {},          //业务传递的JSON内容,即为服务中的JSONObject类型的params对象
	 * 		"paramsType"   : "string",    //可选值string/json,描述params的类型,默认为string，该参数也规定了服务端返回值与返回类型一直
	 * 		"compressFlag" : "N"          //是否压缩，只有当paramsType=string是有效，如果数据量较大时，可以选用gzip压缩，会将params参数压缩传递
	 * }
	 * @return 强制要求返回ResponseResult类型结果
	 * 成功返回ResponseSuccess
	 * 失败返回ResponseError，可以自定义错误code
	 * 返回的数据内容会被服务端以协议格式的内容传递，格式如下
	 * {
	 * 		"messageId"    : "请求消息ID",   //原样返回请求端的消息ID
	 *      "message"      : {}            //业务返回的内容
	 * 		"error"        : "错误原因"      //错误原因
	 *      "status"       : 0             //成功返回0 ，非0为失败
	 * }
	 * 
	 */
	@RequestMapping(value = "/testMessageService")
	public ResponseResult testMessageService(@RequestBody(required = false) JSONObject params){
		return new ResponseSuccess(params);
	}

	/**
	 * 需要POM依赖common-upload
	 * 需要注解@EnableFileUpload
	 * 通过样例中方法可以获取客户端上传的文件列表UploadFile对象
	 * 实际应用中需要对文件进行保存处理需要实现 ApplicationListener<UploadFileEvent>接口，在接口中返回的UploadFile对象body属性为文件字节数组
	 * uploadFileHandle.uploadFile() 返回的参数中UploadFile的body为null
	 * @return
	 */
	@RequestMapping(value = "/testFileUpload" , method = RequestMethod.POST)
	public ResponseResult testFileUploadService(){
		return new ResponseSuccess(uploadFileHandle.uploadFile());
	}
	
	/**
	 * 需要POM依赖common-sqler
	 * 需要注解@EnableSqler
	 * 配置中必须配置数据源
	 * spring启动后会得到ScriptService对象，通过scriptService.execute方法可以执行以下数据库配置的脚本
	 * SEARCH_OBJECT         返回单个对象，Map<String，Object>
	 * SEARCH_LIST           返回一个列表，List<Map<String,Object>>
	 * SEARCH_PAGE           返回一个Page对象
	 * SEARCH_LINK           可以通过配置串连多个SEARCH_*的查询，绑定在一个主对象的属性上返回成一个组合对象。
	 * UPDATE_SQL            执行一个insert,update,delete的更新
	 * GROOVY_SCRIPT         执行一个groovy的脚本，脚本中可以通过dao对象，获取数据库操作能力
	 * @return
	 */
	@RequestMapping(value = "/testScriptService" , method = RequestMethod.POST)
	public ResponseResult testScriptService(){
		return new ResponseSuccess(scriptService.execute("test_script"));
	}
	
	/**
	 * 需要POM依赖common-dao
	 * 通过spring管理的DaoUtil类可以执行SQL语句
	 * @return
	 */
	@RequestMapping(value = "/testDaoService" , method = RequestMethod.POST)
	public ResponseResult testDaoService(){
		return new ResponseSuccess(daoUtil.queryPage("select * from ams_sys_user", 1, 5));
	}
	
	/**
	 * 动态刷新配置需要POM依赖spring-boot-starter-actuator
	 * 远程配置需要POM依赖spring-cloud-config-client
	 * 通过注解@Value获取配置中参数
	 * 通过给应用/actuator/refresh发送POST可以让应用动态更新远程配置
	 * 具体配置参考配置中的注释
	 * @return
	 */
	@RequestMapping(value = "/testConfigValueService" , method = RequestMethod.GET)
	public ResponseResult testConfigValueService(){
		return new ResponseSuccess(testValue);
	}
	
	/**
	 * 需要POM依赖common-redis
	 * 需要配置连接Redis
	 * 测试Redis存入:
	 * {
	 * 		"type":"set",
	 * 		"key":"键",
	 * 		"value":"值",
	 * }
	 * 测试Redis取出:
	 * {
	 * 		"type":"get",
	 * 		"key":"键"
	 * }
	 */
	@RequestMapping(value = "/testRedisService" , method = RequestMethod.GET)
	public ResponseResult testRedisService(@RequestBody(required=false) JSONObject params){
		if("set".equalsIgnoreCase(params.getString("type"))){
			return new ResponseSuccess(redisUtil.set(params.getString("key"), params.getString("value"), 60*60*2l));
		}else{
			return new ResponseSuccess(redisUtil.get(params.getString("key")));
		}
	}

	/**
	 * 需要POM依赖spring-cloud-starter-netflix-hystrix
	 * 服务降级，如果一个服务在一段时间未响应，为了防止其带来服务器宕机的危害，将其做服务降级处理
	 * @return
	 */
	@HystrixCommand(fallbackMethod = "serviceFallback",
		//覆盖配置中的超时隔离策略
			commandProperties = @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"))
	@RequestMapping(value="/testHystrixService",method={RequestMethod.POST,RequestMethod.GET})
	public ResponseResult testHystrixService(){
		try {
			Thread.sleep(1000*20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}
	
	public ResponseResult serviceFallback(){
		return new ResponseSuccess("serviceFallback！");
	}
}
