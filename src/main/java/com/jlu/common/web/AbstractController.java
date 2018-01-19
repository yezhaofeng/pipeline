package com.jlu.common.web;

import org.apache.commons.lang.StringUtils;

/**
 * Created by langshiquan on 18/1/10.
 */
public abstract class AbstractController {
    protected String getLoginUserName() {
        return StringUtils.EMPTY + "unkown";
    }
}
