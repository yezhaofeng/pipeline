package com.jlu.plugin.runtime;

import com.jlu.plugin.runtime.bean.FormType;
import com.jlu.plugin.runtime.service.DefaultPluginValueGenerator;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2019/1/23.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RuntimeRequire {
    Class defaultValueClass() default DefaultPluginValueGenerator.class;

    String description() default "无描述信息";

    FormType formType() default FormType.TEXT;

    String checkRegex() default "\\S+";

}
