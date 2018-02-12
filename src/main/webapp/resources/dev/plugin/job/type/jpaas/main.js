/**
 * @file JPaaS online job plugin
 */
define(['app-plugin-job', 'constants'], function (app, constants) {
    'use strict';

    app.controller('JpaasPluginController', ['$http', '$scope', '$interval', JpaasPluginController]);

    function JpaasPluginController($http, $scope, $interval) {
        var self = this;

    }

    app.directive('appPluginJobJpaasJobSelector', [function () {
        return {
            restrict: 'AE',
            replace: true,
            scope: true,
            link: function (scope, element, attrs) {
                scope.jobs = [{
                    name: 'JPaaS-Test',
                    jpaasType: 'TEST'
                }, {
                    name: 'JPaaS线上',
                    jpaasType: 'ONLINE'
                }];
            }
        };
    }]);


});