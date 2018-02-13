/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file angularjs 指令：修改验证结果
 */

/**
 * @param app: angular.Module
 */
define(['../app'], function appValid(app) {
    app.directive('appValid',
        [
            function () {
                return {
                    link: function (scope, el, attr, model) {
                        scope[attr.ngForm].$setValidity = function () {};
                    }
                };
            }
        ]
    );
});
