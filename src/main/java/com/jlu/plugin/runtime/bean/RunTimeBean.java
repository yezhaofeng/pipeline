package com.jlu.plugin.runtime.bean;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;


/**
 * Created by Administrator on 2018/1/25.
 */
public class RunTimeBean {

    String name;
    String formType;
    Object defaultValue;
    String checkRegex;
    String description;

    public String getName() {
        return name;
    }

    public RunTimeBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getFormType() {
        return formType;
    }

    public RunTimeBean setFormType(String formType) {
        this.formType = formType;
        return this;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public RunTimeBean setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getCheckRegex() {
        return checkRegex;
    }

    public RunTimeBean setCheckRegex(String checkRegex) {
        this.checkRegex = checkRegex;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RunTimeBean setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, Object> toRuntimeMap() {
        Map<String, Object> map = new HashedMap();
        map.put(this.name, this.defaultValue);
        return map;
    }
    @Override
    public String toString() {
        return "RunTimeBean{" +
                "name='" + name + '\'' +
                ", formType=" + formType +
                ", defaultValue=" + defaultValue +
                ", checkRegex='" + checkRegex + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
