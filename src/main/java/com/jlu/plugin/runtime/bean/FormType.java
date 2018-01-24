package com.jlu.plugin.runtime.bean;

/**
 * Created by Administrator on 2018/1/25.
 */
public enum FormType {
    TEXT("text"), TEXTAREA("textarea"), CHECK_BOX("checkbox");

    FormType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

}
