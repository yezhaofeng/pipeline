package com.jlu.pipeline.job.bean;

/**
 * Created by Administrator on 2018/1/19.
 */
public class JobParameter {
    // 提交信息
    public static final String PIPELINE_MODULE = "PIPELINE_MODULE_NAME";
    public static final String PIPELINE_BRANCH = "PIPELINE_BRANCH";
    public static final String PIPELINE_COMMIT_ID = "PIPELINE_COMMIT_ID";
    public static final String PIPELINE_COMMIT_COMMENTS = "PIPELINE_COMMIT_COMMENTS";
    public static final String PIPELINE_BUILD_ID = "PIPELINE_BUILD_ID";
    public static final String PIPELINE_BUILD_NUMBER = "PIPELINE_BUILD_NUMBER";
    public static final String PIPELINE_TRIGGER_MODE = "PIPELINE_TRIGGER_MODE";
    public static final String PIPELINE_START_TIME = "PIPELINE_START_TIME";
    public static final String PIPELINE_REPOSITORY_GITHUB_URL = "PIPELINE_REPOSITORY_GITHUB_URL";
    public static final String PIPELINE_TRIGGER_USER = "PIPELINE_TRIGGER_USER";
    public static final String PIPELINE_CHECKIN_AUTHOR = "PIPELINE_CHECKIN_AUTHOR";

    // 产出信息
    public static final String PIPELINE_COMPILE_PRODUCT_PATH = "PIPELINE_COMPILE_PRODUCT_PATH";
    public static final String PIPELINE_RELEASE_PRODUCT_PATH = "PIPELINE_RELEASE_PRODUCT_PATH";
    public static final String PIPELINE_RELEASE_VERSION = "PIPELINE_RELEASE_VERSION";
}
