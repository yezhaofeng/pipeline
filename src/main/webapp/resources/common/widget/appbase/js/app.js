/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
/**
 * @file base
 * @author baidu
 */

define(['angular'], function (angualr) {

    var tool = {
        stringLength: function (str) {
            if (!str) {
                return 0;
            }
            var len = 0;
            for (var i = 0; i < str.length; i++) {
                var c = str.charCodeAt(i);
                if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
                    len++;
                } else {
                    len += 2;
                }
            }
            return len;
        }
    };


    return angualr
        .module('app.base', [])
        .filter('appTextOverflow', function () {
            return function (str, limit, suffix) {
                str = str || '';
                suffix = suffix || '';
                return str.length > limit ? str.substr(0, limit - suffix.length) + suffix : str;
            };
        })
        .filter('appTextDecode', function () {
            return function (str) {
                return decodeURI(str);
            };
        })
        .directive('appListMinLength', [
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
        ])
        .directive('appValid', [
            function () {
                return {
                    link: function (scope, el, attr, model) {
                        scope[attr.ngForm].$setValidity = function () {};
                    }
                };
            }
        ])
        .directive('appInitAfterShow', [
            function () {
                return {
                    restrict: 'A',
                    scope: false,
                    link: function (scope, el, attr) {
                        scope.$watch(function () {
                            return el.is(':visible');
                        }, function (visible) {
                            visible && scope.$eval(attr.appInitAfterShow);
                        });
                    }
                };
            }
        ])
        .directive('appStringMaxLength', function () {
            return {
                require: '?ngModel',
                restrict: 'AE',
                link: function (scope, el, attr, ctrl) {

                    if (!ctrl) {
                        return;
                    }

                    var max = 0;
                    attr.$observe('appStringMaxLength', function (value) {
                        max = parseInt(value, 10) || 0;
                        ctrl.$validate();
                    });
                    ctrl.$validators.stringMaxLength = function (modelValue, viewValue) {
                        var length = tool.stringLength(modelValue) || 0;
                        return length <= max;
                    };
                }
            };
        });

});
