/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
/**
 * @file base
 * @author baidu
 */

define(['angular'], function (angualr) {
    return angualr
        .module('app.interval', [])
        .service('appIntervalService', [
            '$q',
            '$interval',
            '$timeout',
            '$rootScope',
            function ($q, $interval, $timeout, $rootScope) {
                var self = this;

                /**
                 * 串行：积压时，延时到当前任务结束后立即触发一次
                 * @param {Object} scope $rootScope.Scope
                 * @param {Function} task 任务
                 * @param {Function} delay 延迟毫秒
                 * @return {Object} interval 策略
                 */
                var interval = function (scope, task, delay) {
                    var instance = {
                        interval: undefined,
                        refresh: false,
                        refreshing: false,
                        start: function () {
                            // TODO
                            return;
                            instance.refresh = false;
                            instance.refreshing = false;
                            var process = function () {
                                $q.all([task()]).then(function () {
                                    if (instance.refresh) {
                                        instance.refresh = false;
                                        process();
                                    } else {
                                        instance.refreshing = false;
                                    }
                                });
                            };
                            instance.interval = $interval(function () {
                                if (instance.refreshing) {
                                    instance.refresh = true;
                                } else {
                                    instance.refresh = false;
                                    instance.refreshing = true;
                                    process();
                                }
                            }, delay);
                        },
                        stop: function () {
                            // TODO
                            return;
                            instance.interval && $interval.cancel(instance.interval);
                            instance.interval = undefined;
                        }
                    };
                    return instance;
                };

                /**
                 * 串行：上次任务结束后延时delay毫秒再触发下次
                 * @param {Object} scope $rootScope.Scope
                 * @param {Function} task 任务
                 * @param {Function} delay 延迟毫秒
                 * @return {Object} timeout 策略
                 */
                var timeout = function (scope, task, delay) {
                    var instance = {
                        timeout: undefined,
                        start: function () {
                            var process = function () {
                                $q.all([task()]).then(function () {
                                    instance.timeout = $timeout(process, delay);
                                });
                            };
                            instance.timeout = $timeout(process, delay);
                        },
                        stop: function () {
                            instance.timeout && $timeout.cancel(instance.timeout);
                            instance.timeout = undefined;
                        }
                    };
                    return instance;
                };

                var strategy = function (strategy, scope, task, delay) {
                    if (scope instanceof Object && task instanceof Function) {
                        delay = angular.isNumber(delay) && delay > 3000 ? delay : 3000;
                        var interval = strategy(scope, task, delay);
                        interval.start();
                        scope.$on('$destroy', interval.stop);
                    } else {
                        throw 'Illegal scope(arguments[0]) or task(arguments[1] !)';
                    }
                };

                self.interval = function (scope, task, delay) {
                    strategy(interval, scope, task, delay);
                };

                self.timeout = function (scope, task, delay) {
                    strategy(timeout, scope, task, delay);
                };
            }
        ]);
});
