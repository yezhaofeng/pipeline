package com.jlu.common.exception;


//@I18NResourceBundle( baseName = "com/baidu/scm/resources/messages",   defaultLocale = "zh_CN" ) 
public interface I18nMsgKey {
	
	 /*
	  * ######################################
	  * 通用的错误信息,以common.开头
	  * ######################################
	  */
	 ////@I18NMessage("对不起！当前系统执行错误，原因： {0}。 当前时间： {1}") 
	 String COMMON_ERROR_INFO="common.html.error.info";
	 
	 //@I18NMessage("常规版本") 
	 String COMMON_FOURVERSION_COMMON="common.fourversion.common";
	 
	 //@I18NMessage("非常规版本") 
	 String COMMON_FOURVERSION_STUDY="common.fourversion.study";
	 
	 //@I18NMessage("小流量版本") 
	 String COMMON_FOURVERSION_PARTONLINE="common.fourversion.partonline";
	 //@I18NMessage("操作成功") 
	 String COMMON_SUCCESS="common.success";
	 //@I18NMessage("操作失败") 
	 String COMMON_ERROR="common.html.error";
	 
	 String COMMON_NODATA="common.nodata";
	 
	 //@I18NMessage("参数错误") 
	 String COMMON_PARAM_ERROR="common.param.html.error";
	 //@I18NMessage("当前没有可显示的记录") 
	 String PAGINATCION_NODATA ="pagination.nodata";
	 //@I18NMessage("总共<b>{0}</b>页,<b>{1}</b>条记录") 
	 String PAGINATION_INFO="pagination.info";
	 //@I18NMessage("全部") 
	 String PAGINATION_VIEWALL="pagination.viewall";
	 
	 String PAGINATION_SPLIT="pagination.split";
	 
	 //@I18NMessage("没有选择任何元素")
	 String NOT_SELECT_ITEM="common.not.select.item";
	 
	 //@I18NMessage("{0}对{1}无操作权限")
	 String PERMISSION_ACCESS_DENIED_USER="permission.exception.access.denied.user";
	 
	 /*
	  * ######################################
	  * 编译的错误信息,以compiler.开头
	  * ######################################
	  */

}
