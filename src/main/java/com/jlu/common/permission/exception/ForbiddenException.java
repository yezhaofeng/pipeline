package com.jlu.common.permission.exception;

/**
 * Created by yezhaofeng on 2019/1/28.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
