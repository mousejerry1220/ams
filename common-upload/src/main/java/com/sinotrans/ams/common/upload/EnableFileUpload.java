package com.sinotrans.ams.common.upload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 * 使用注解后文件上传的内容会通过ApplicationEventPublisher通过事件形式发送出来，客户端需要实现接口ApplicationListener<UploadFileEvent>来对文件做保存等处理
 * @author Jerry.Zhao
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({UploadFileHandle.class,StandardServletMultipartResolver.class})
public @interface EnableFileUpload {
	
}
