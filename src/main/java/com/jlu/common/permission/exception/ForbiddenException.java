package com.jlu.common.permission.exception;

/**
 * Created by langshiquan on 18/1/28.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
