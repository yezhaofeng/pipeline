package com.jlu.jenkins.exception;

/**
 * Created by langshiquan on 18/1/10.
 */
public class JenkinsException extends RuntimeException {

    public JenkinsException(JenkinsExceptionEnum type, Exception e) {
        super(type.name() + ":" + type.message + "," + e.getMessage());
    }

    public JenkinsException(JenkinsExceptionEnum type) {
        super(type.name() + ":" + type.message);
    }
}
