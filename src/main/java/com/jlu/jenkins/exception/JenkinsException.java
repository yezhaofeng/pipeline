package com.jlu.jenkins.exception;

import com.jlu.common.exception.PipelineRuntimeException;

/**
 * Created by langshiquan on 18/1/10.
 */
public class JenkinsException extends PipelineRuntimeException {

    public JenkinsException(JenkinsExceptionEnum type, Exception e) {
        super(type.name() + ":" + type.message + "," + e.getMessage());
    }

    public JenkinsException(String message) {
        super(message);
    }

    public JenkinsException(JenkinsExceptionEnum type) {
        super(type.name() + ":" + type.message);
    }
}
