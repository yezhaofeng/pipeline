package com.jlu.common.web;

import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.interceptor.UserLoginHelper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Created by langshiquan on 18/1/10.
 */
public abstract class AbstractController {
    protected String getLoginUserName() {
        return UserLoginHelper.getLoginUserName();
    }

    protected void checkBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<ObjectError> errorList = result.getAllErrors();
            for (ObjectError error : errorList) {
                sb.append(error.getDefaultMessage() + " ");
            }
            throw new PipelineRuntimeException(sb.toString());
        }
    }
}
