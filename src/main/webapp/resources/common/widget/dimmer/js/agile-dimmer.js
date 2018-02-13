/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file 遮罩层,用于引导页遮罩或者其他需要遮罩的地方
 * @author liming16
 */

define(['angular'], function (angular) {

    'use strict';

    angular.module('agile.dimmer', [])

        .directive('agileDimmer', function () {
            return {
                restrict: 'AE',
                replace: true,
                transclude: true,
                scope: {
                    showDimmer: '='
                },
                template: '<div class=\"{{dimmerClass}}\" >'
                + '<div class=\"header\"><span class=\"close-dimmer\" ng-click=\"clickOnDimmer()\"></span></div>'
                + '<div class=\"content\">'
                + '<div class=\"content-body\" ng-transclude></div>'
                + '</div>'
                + '</div>',
                link: function (scope, element, attrs, ngModel) {

                    //
                    // Click on dimmer handler
                    //
                    scope.clickOnDimmer = function () {
                        scope.showDimmer = false;
                        scope.dimmerClass = 'agile page dimmer';
                    };

                    //
                    // Watch for the ng-model changing
                    //
                    scope.$watch('showDimmer', function (val) {
                        if (val === false || val === undefined) {
                            scope.dimmerClass = 'agile page dimmer';
                        }
                        else {
                            scope.dimmerClass = 'agile page active dimmer';
                        }

                    });
                }
            };


        });
});


