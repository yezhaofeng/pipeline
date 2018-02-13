/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file job plugin module
 */

define('app-plugin-job', ['angular', 'agile-codemirror', 'agile-loading'], function (angular) {
    return angular.module('plugin.job', ['agile.codemirror', 'agile.loading']);
});
