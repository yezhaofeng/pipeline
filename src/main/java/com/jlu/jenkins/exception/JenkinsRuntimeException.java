package com.jlu.jenkins.exception;

/**
 * Created by langshiquan on 18/1/10.
 */
public class JenkinsRuntimeException extends RuntimeException {

    public JenkinsRuntimeException(JenkinsRuntimeExceptionEnum type, Exception e) {
        super(type.name() + ":" + type.message + "," + e.getMessage());
    }

    public JenkinsRuntimeException(JenkinsRuntimeExceptionEnum type) {
        super(type.name() + ":" + type.message);
    }
}
