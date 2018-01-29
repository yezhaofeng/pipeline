package com.jlu.common.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.jlu.common.exception.ForbiddenException;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.web.ResponseBean;

/**
 * Created by langshiquan on 18/1/28.
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(ForbiddenException.class)
    public ModelAndView forbiddenExceptionHandler(ForbiddenException fbex) {
        ModelAndView mv = new ModelAndView("403");
        mv.addObject("message", fbex.getMessage());
        return mv;
    }

    @ExceptionHandler(PipelineRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean pipelineRuntimeExceptionHandler(PipelineRuntimeException pre) {
        return ResponseBean.fail(pre.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseBean exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.info("request {} error:", request.getRequestURI(), e);
        return ResponseBean.fail(e.getMessage());
    }

}
