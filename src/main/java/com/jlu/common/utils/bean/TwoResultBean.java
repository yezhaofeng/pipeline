package com.jlu.common.utils.bean;

public class TwoResultBean<S,M> {

    private S status;
    private M message;

    public S getStatus() {
        return status;
    }

    public void setStatus(S status) {
        this.status = status;
    }

    public M getMessage() {
        return message;
    }

    public void setMessage(M message) {
        this.message = message;
    }

    public TwoResultBean(S status, M message) {
        this.status = status;
        this.message = message;
    }
}
