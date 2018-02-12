/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file 插件初始化
 */

define(
    [
        // inits
        '../plugin/job/common/plugin-job-init',
        // services
        '../plugin/job/common/plugin-job-service',
        // ctrls
        // dircts
        '../plugin/job/config/plugin-job-config',
        '../plugin/job/build/real-job-build-brief',
        '../plugin/job/build/real-job-build',
        '../plugin/job/build/job-execute-require'
    ],
    function (initPromise) {
        return initPromise;
    }
);
