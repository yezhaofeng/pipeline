/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
/**
 * @file 拷贝到粘贴板功能封装
 *
 * 使用方式 任意元素上添加 agile-copy 属性, 并指定 data-clipboard-target 的目标
 * 例如 <div id="content">Some contents<div> <button agile-copy data-clipboard-target="#content">Copy</button>
 *
 * @author liming16
 */

define(['clipboard', 'angular'], function (Clipboard, angualr) {

    angualr.module('agile.clipboard', [])
        .directive('agileCopy', ['$log', '$window', function ($log, $window) {
            return {
                restrict: 'AE',
                replace: true,
                scope: {
                    clipboard: '=appClipboard',
                    judgeColor: '=judgeColor'
                },
                template: '<span class="tooltip-top {{getJudgeColor()}}" tooltip-data="复制"' +
                ' data-clipboard="{{clipboard}}" style="cursor: pointer"><i class="fa fa-clone"></i></span>',
                link: function (scope, element, attr) {
                    var cpboard = new Clipboard(element[0], {
                        text: function (trigger) {
                            return angular.element(element).data('clipboard');
                        }
                    });
                    scope.getJudgeColor = function () {
                        return scope.judgeColor ? 'agile-clipboard-gray-color' : 'agile-clipboard-default-color';
                    };
                    cpboard.on('success', function (e) {
                        e.clearSelection();
                        angular.element(element).addClass('tooltip-top tooltip-rounded');
                        angular.element(element).attr('tooltip-data', '已复制');
                        setTimeout(function () {
                            angular.element(element).removeClass('tooltip-top tooltip-rounded');
                            angular.element(element).removeAttr('tooltip-data');
                        }, 500);
                    });
                    cpboard.on('error', function (e) {
                        angular.element(element).addClass('tooltip-top tooltip-rounded');
                        angular.element(element).attr('tooltip-data', '复制出错,请手动选中复制');
                        setTimeout(function () {
                            angular.element(element).removeClass('tooltip-top tooltip-rounded');
                            angular.element(element).removeAttr('tooltip-data');
                        }, 500);
                    });

                    element.on('$destroy', function () {
                        cpboard.destroy();
                    });

                }
            }
        }]);

});