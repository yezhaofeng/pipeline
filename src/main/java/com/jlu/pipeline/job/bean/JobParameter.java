package com.jlu.pipeline.job.bean;

/**
 * Created by Administrator on 2018/1/19.
 */
public class JobParameter {
    // 提交信息
    public final String PIPELINE_MODULE = "PIPELINE_MODULE";
    public final String PIPELINE_BRANCH = "PIPELINE_BRANCH";
    public final String PIPELINE_COMMIT_ID = "PIPELINE_COMMIT_ID";
    public final String PIPELINE_BUILD_NUMBER = "PIPELINE_BUILD_NUMBER";
    public final String PIPELINE_TRIGGER_USER = "PIPELINE_TRIGGER_USER";
    public final String PIPELINE_TRIGGER_MODE = "PIPELINE_TRIGGER_MODE";
    public final String PIPELINE_START_TIME = "PIPELINE_START_TIME";

    // 产出信息
    public final String PIPELINE_COMPILE_PRODUCT_PATH = "PIPELINE_COMPILE_PRODUCT_PATH";
    public final String PIPELINE_RELEASE_PRODUCT_PATH = "PIPELINE_RELEASE_PRODUCT_PATH";


}
