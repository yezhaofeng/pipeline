package com.jlu.jenkins.exception;

/**
 * Created by langshiquan on 18/1/10.
 */
public enum JenkinsRuntimeExceptionEnum {
    WRONG_URL("Jenkins URL不合法"), SERVER_INIT_FAILED("Jenkins服务不在线或者帐号密码错误"),
    NETWORK_UNREACHABLE("通讯异常"), NOT_FOUND_JOB("未找到Job"), SLAVE_OFFLINE("slave节点已下线");

    String message;

    JenkinsRuntimeExceptionEnum(String message) {
        this.message = message;
    }
}
