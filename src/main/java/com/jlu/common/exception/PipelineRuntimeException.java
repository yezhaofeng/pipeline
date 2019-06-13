package com.jlu.common.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
public class PipelineRuntimeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -442802263750159377L;
    private List<I18nMsg> errorMsgs = null;
    private I18nMsg errorMsg = null;

    /**
     *
     */
    public PipelineRuntimeException() {
        super();
    }

    /**
     * @param key
     * @param varargs
     */
    public PipelineRuntimeException(String key, Object... varargs) {
        super(I18nMsg.createMsg(key, varargs).toString());
        errorMsg = new I18nMsg(key, varargs);
        this.addErrorMsg(errorMsg);
    }

    /**
     * @param cause
     * @param key
     * @param varargs
     */
    public PipelineRuntimeException(Throwable cause, String key, Object... varargs) {
        super(I18nMsg.createMsg(key, varargs).toString(), cause);

        errorMsg = new I18nMsg(key, varargs);
        this.addErrorMsg(errorMsg);
    }

    /**
     * @param cause
     */
    public PipelineRuntimeException(Throwable cause) {
        super(cause);
    }

    public void addErrorMsg(I18nMsg msg) {
        if (this.errorMsgs == null) {
            this.errorMsgs = new ArrayList<I18nMsg>();
        }
        this.errorMsgs.add(msg);
    }

    public void addErrorMsgs(List<I18nMsg> msg) {
        if (this.errorMsgs == null) {
            this.errorMsgs = new ArrayList<I18nMsg>();
        }
        this.errorMsgs.addAll(msg);
    }

    public void addErrorMsg(String key, Object... varargs) {
        this.addErrorMsg(new I18nMsg(key, varargs));
    }

    /**
     * 指定Locale获取对应的错误本地化信息，主要用在页面展现异常信息是需要改版Locale与当前用户一致的情况下
     *
     * @param locale
     *
     * @return
     */
    public String getErrorMessage(Locale locale) {
        if (errorMsg == null) {
            return super.getLocalizedMessage();
        }

        return errorMsg.getShowMessage(locale);
    }

    public List<String> getErrorMessageList(Locale locale) {
        List<String> errorMessageList = new ArrayList<String>();

        for (I18nMsg msg : errorMsgs) {
            errorMessageList.add(msg.getShowMessage(locale));
        }
        return errorMessageList;
    }

    public List<I18nMsg> getErrorI18nMsgs() {
        return errorMsgs;
    }

    /**
     * 用来显示在页面的错误信息，根据用户的当前Locale信息。
     *
     * @return
     */
    public List<String> getUIShowMessageList() {

        return this.getErrorMessageList(MessageHolder.getLocale());
    }

    /**
     * 获取用户当前显示的LOCALE一致的错误信息
     *
     * @return
     */
    public String getUIShowMessage() {

        return this.getErrorMessage(MessageHolder.getLocale());
    }
}
