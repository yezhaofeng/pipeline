package com.jlu.common.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jlu.common.aop.utils.AccessLogHelper;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.permission.exception.ForbiddenException;
import com.jlu.common.web.ResponseBean;

/**
 * Created by langshiquan on 18/1/28.
 */
@ControllerAdvice
public class ExceptionControllerAdvice {


    // 403
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseBean forbiddenExceptionHandler(ForbiddenException fbex) {
        return ResponseBean.fail(fbex.getMessage());
    }

    // 400
    @ExceptionHandler(PipelineRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseBean pipelineRuntimeExceptionHandler(PipelineRuntimeException pre) {
        return ResponseBean.fail(pre.getMessage());
    }

    // 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseBean exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        AccessLogHelper.logAccessErrorOut(request, response, e);
        return ResponseBean.fail(e.getMessage());
    }

}
