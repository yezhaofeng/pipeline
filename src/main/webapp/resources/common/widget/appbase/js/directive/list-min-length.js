/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file angularjs 指令：验证List的最小length
 */

/**
 * @param app: angular.Module
 */
define(['../app'], function appListMinLength(app) {
    app.directive('appListMinLength',
        [
            function () {
                return {
                    require: 'ngModel',
                    link: function (scope, el, attr, model) {
                        var min = attr.appListMinLength || 0;
                        var customValidator = function () {
                            var length = model.$modelValue || 0;
                            var validator = length >= min;
                            model.$setValidity('listMinLength', validator);
                        };
                        model.$formatters.push(customValidator);
                        model.$parsers.push(customValidator);
                    }
                };
            }
        ]
    );
});
