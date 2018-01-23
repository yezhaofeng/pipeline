package com.jlu.plugin.input;

/**
 * Created by Administrator on 2018/1/24.
 */
public enum InputEnum {
    TEXT("text"), CHECK_BOX("checkbox"),;
    String type;

    InputEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
