package com.jlu.jenkins.exception;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
public enum JenkinsExceptionEnum {
    WRONG_URL("Jenkins URL不合法"), SERVER_INIT_FAILED("Jenkins服务不在线或者帐号密码错误"),
    NETWORK_UNREACHABLE("通讯异常"), NOT_FOUND_JOB("未找到Job"), SLAVE_OFFLINE("slave节点已下线"), UNKOWN("调起Job失败"),
    SLAVE_NOT_FOUND("未找到slave节点"),
    NOT_SUPPORT_CONCURRENCY("此Job不支持并发构建，若想并发构建，请在Jenkins Job配置中勾选'在必要的时候并发构建'");

    String message;

    JenkinsExceptionEnum(String message) {
        this.message = message;
    }
}
