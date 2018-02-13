/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file Agile CodeMirror 组件
 * @author liming16
 */
define(['codemirror', 'angular'], function (CodeMirror, angular) {

    'use strict';
    return angular.module('agile.codemirror', [])
        .constant('agileCodemirrorConfig', {})
        .directive('agileCodemirror', ['$timeout', 'agileCodemirrorConfig', agileCodeMirrorDirective]);

    function agileCodeMirrorDirective($timeout, agileCodemirrorConfig) {

        return {
            restrict: 'A',
            require: '?ngModel',

            compile: function compile() {
                // require CodeMirror
                /* if (angular.isUndefined(CodeMirror)) {
                 throw new Error('agile-codemirror needs CodeMirror to work... (o rly?)');
                 }*/
                return postLink;
            }
        };

        function postLink(scope, element, iAttrs, ngModel) {

            var codemirrorOptions = angular.extend(
                {value: element.text()},
                agileCodemirrorConfig.codemirror || {},
                scope.$eval(iAttrs.agileCodemirror),
                scope.$eval(iAttrs.agileCodemirrorOpts)
            );

            var codemirror = newCodemirrorEditor(element, codemirrorOptions);

            configOptionsWatcher(
                codemirror,
                iAttrs.agileCodemirror || iAttrs.agileCodemirrorOpts,
                scope
            );

            configNgModelLink(codemirror, ngModel, scope);

            configRefreshAttribute(codemirror, iAttrs.agileRefresh, scope);

            // Allow access to the CodeMirror instance through a broadcasted event
            // eg: $broadcast('CodeMirror', function(cm){...});
            scope.$on('CodeMirror', function (event, callback) {
                if (angular.isFunction(callback)) {
                    callback(codemirror);
                } else {
                    throw new Error('the CodeMirror event requires a callback function');
                }
            });

            // onLoad callback
            if (angular.isFunction(codemirrorOptions.onLoad)) {
                codemirrorOptions.onLoad(codemirror);
            }
        }

        function newCodemirrorEditor(element, codemirrorOptions) {
            var codemirrorEditor;
            if (element[0].tagName === 'TEXTAREA') {
                // Might bug but still ...
                codemirrorEditor = CodeMirror.fromTextArea(element[0], codemirrorOptions);
            } else {
                element.html('');
                codemirrorEditor = new CodeMirror(function (cmEl) {
                    element.append(cmEl);
                }, codemirrorOptions);
            }

            return codemirrorEditor;
        }

        function configOptionsWatcher(codemirrorEditor, agileCodemirrorAttr, scope) {
            if (!agileCodemirrorAttr) {
                return;
            }

            var codemirrorDefaultsKeys = Object.keys(CodeMirror.defaults);
            scope.$watch(agileCodemirrorAttr, updateOptions, true);
            function updateOptions(newValues, oldValue) {
                if (!angular.isObject(newValues)) {
                    return;
                }
                codemirrorDefaultsKeys.forEach(function (key) {
                    if (newValues.hasOwnProperty(key)) {

                        if (oldValue && newValues[key] === oldValue[key]) {
                            return;
                        }

                        codemirrorEditor.setOption(key, newValues[key]);
                    }
                });
            }
        }

        function configNgModelLink(codemirror, ngModel, scope) {
            if (!ngModel) {
                return;
            }

            ngModel.$formatters.push(function (value) {
                if (angular.isUndefined(value) || value === null) {
                    return '';
                } else if (angular.isObject(value) || angular.isArray(value)) {
                    throw new Error('agile-codemirror cannot use an object or an array as a model');
                }
                return value;
            });


            ngModel.$render = function () {
                var safeViewValue = ngModel.$viewValue || '';
                codemirror.setValue(safeViewValue);
            };

            // Keep the ngModel in sync with changes from CodeMirror
            codemirror.on('change', function (instance) {
                var newValue = instance.getValue();
                if (newValue !== ngModel.$viewValue) {
                    scope.$evalAsync(function () {
                        ngModel.$setViewValue(newValue);
                    });
                }
            });

        }

        function configRefreshAttribute(codeMirror, agileRefreshAttr, scope) {
            if (!agileRefreshAttr) {
                return;
            }

            scope.$watch(agileRefreshAttr, function (newVal, oldVal) {
                // Skip the initial watch firing
                if (newVal !== oldVal) {
                    $timeout(function () {
                        codeMirror.refresh();
                    });
                }
            });
        }

    }
});