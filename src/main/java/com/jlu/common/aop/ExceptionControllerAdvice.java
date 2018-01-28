package com.jlu.common.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.jlu.common.exception.ForbiddenException;

/**
 * Created by langshiquan on 18/1/28.
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public ModelAndView exceptionHandler(ForbiddenException fbex) {
        ModelAndView mv = new ModelAndView("403");
        mv.addObject("message", fbex.getMessage());
        return mv;
    }
}
