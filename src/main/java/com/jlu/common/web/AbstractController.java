package com.jlu.common.web;

import com.jlu.common.interceptor.UserLoginHelper;

/**
 * Created by langshiquan on 18/1/10.
 */
public abstract class AbstractController {
    protected String getLoginUserName() {
        return UserLoginHelper.getLoginUserName();
    }
}
