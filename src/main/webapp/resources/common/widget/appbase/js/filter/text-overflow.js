/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
/**
 * @file app
 * @author baidu
 */

define(['../app'], function (app) {
    return app.filter('appTextOverflow', function () {
        return function (str, limit, suffix) {
            str = str || '';
            suffix = suffix || '';
            return str.length > limit ? str.substr(0, limit - suffix.length) + suffix : str;
        };
    });
});
