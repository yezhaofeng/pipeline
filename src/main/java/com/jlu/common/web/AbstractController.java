package com.jlu.common.web;

import com.jlu.common.cookies.LoginHelper;

/**
 * Created by langshiquan on 18/1/10.
 */
public abstract class AbstractController {
    // TODO
    protected String getLoginUserName() {
        return LoginHelper.getLoginerUserName();
    }
}
