/**
 * 
 */
package com.jlu.common.exception;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * @author fangjianping
 * 国际化消息对象
 */
public class I18nMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private Object[] vars;
	
	
	/*public I18nMsg() {
		super();
		
	}*/

	public I18nMsg(String key, Object... vars) {
		this.key = key;
		this.vars = vars;

	}
	//由zhangjing注释。由于使用可变长参数，会与public I18nMsg(String key, Object... vars) 的构造函数在调用时给开发者造成混淆。考虑其他的构造函数方法
	/*public I18nMsg(String resourceBundleName, String key, Object... vars) {
		this.resourceBundleName = resourceBundleName;
		this.key = key;
		this.vars = vars;
	}*/

	public String getShowMessage(Locale locale) {
		return  MessageHolder.get(key,locale,vars);

	}

	public String getShowMessage() {
		
		return  MessageHolder.get(key, vars);

	}
	public String getShowUIMessage() {
		return this.getShowMessage(MessageHolder.getLocale());
	}
	
	public String toString(){
		return this.getShowMessage();
	}
	
	public static I18nMsg createMsg(String key, Object... varargs)
    {
		I18nMsg msg = new I18nMsg(key,varargs);
        return msg;
    }
	
	public static I18nMsg createMsg(List<I18nMsg> i18nMsgs, String key, Object... varargs)
    {
		I18nMsg msg = new I18nMsg(key,varargs);
		i18nMsgs.add(msg);
		return msg;
    }
	/**
	 * 追加msg
	 * @param key
	 * @param varargs
	 * @return
	 */
	public void appendMsg(String key, Object... varargs)
	{
		I18nMsg msg = new I18nMsg(key,varargs);
		appendMsg(msg);
	}
	/**
	 * 开始处添加msg
	 * @param key
	 * @param varargs
	 * @return
	 */
	public void insertMsg(String key, Object... varargs)
	{
		I18nMsg msg = new I18nMsg(key,varargs);
		insertMsg(msg);
	}
	/**
	 * 追加msg
	 * @param msg
	 * @return
	 */
	public void appendMsg(I18nMsg msg) {
		if (msg != null) {
			key=this.getShowUIMessage() + " " + msg.getShowUIMessage();
			vars=new Object[0];
		} 
	}
	/**
	 * 开始处添加msg
	 * @param msg
	 * @return
	 */
	public void insertMsg(I18nMsg msg) {
		if (msg != null) {
			key=msg.getShowUIMessage() + " " + this.getShowUIMessage();
			vars=new Object[0];
		} 
	}
}
