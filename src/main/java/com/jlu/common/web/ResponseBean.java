package com.jlu.common.web;

/**
 * Created by langshiquan on 18/1/10.
 */
public class ResponseBean {
    private boolean success;

    private String message;

    public static final ResponseBean TRUE = new ResponseBean(true);

    public static ResponseBean fail(String error) {
        ResponseBean response = new ResponseBean(false);
        response.setMessage(error);
        return response;
    }
    public static ResponseBean succ(String message) {
        ResponseBean response = new ResponseBean(true);
        response.setMessage(message);
        return response;
    }


    public ResponseBean(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
