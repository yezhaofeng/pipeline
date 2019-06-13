/**
 * Created by yezhaofeng on 2019/3/17.
 */
/**
 * @file ContextService
 */

define(['app', 'angular', 'constants'], function appPlContextService(app, angular, constants) {
    app.service('pipelineContextService',
        [
            '$q', '$http', '$state', '$window', '$timeout', 'localStorageService', 'pipelineDataService', ContextService
        ]
    );

    function ContextService($q, $http, $state, $window, $timeout, localStorageService, pipelineDataService) {
        var self = this;

        self.context = {
            module: '',
            username: '',
            branchType: 'TRUNK',
        };

        self.setModule = function (module) {
            self.context.module = module;
        };

        self.setUsername = function (username) {
            self.context.username = username;
        };

        self.setBranchType = function (branchType) {
            self.context.branchType = branchType;
        };

        self.selectModule = function (module) {
            self.context.module = module;
            localStorageService.addRecentModule(module);
            self.initContext();
            $state.go(
                'builds.trunk',
                {
                    module: self.context.module
                }
            );
        };

        self.initContext = function () {
            var lastVisitModule = localStorageService.getRecentModule();
            self.context.module = lastVisitModule;
            pipelineDataService.getLoginUserInfo().then(function (data) {
                if (data == null || data == '' || data == undefined) {
                    window.location.href = '/login';
                }
                self.context.username = data.username;
            });
        };
    }
});

