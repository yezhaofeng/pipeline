package com.jlu.common.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 
 * @author lixiangjun
 *
 */
public class CustomResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {
	/**
	 * 获取所有的国际化消息资源.
	 * @param locale 地域信息
	 * @return 国际消息资源Map，key为国际化消息标识，value为国际化消息内容
	 */
	public Properties getAllMessages(Locale locale) {
		PropertiesHolder propertiesHolder = super.getMergedProperties(locale);
		return 	propertiesHolder.getProperties();
	}
	protected String getMessageFromParent(String code, Object[] args, Locale locale) {
		String message = super.getMessageFromParent(code, args, locale);
		if(message ==null){
			message = MessageFormat.format(code, args);
		}
		return message;
	}
}
