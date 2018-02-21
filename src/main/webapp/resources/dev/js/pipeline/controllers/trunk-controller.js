/**
 * Created by Wonpang New on 2016/9/10.
 *
 * 主干流水线
 */

define(['app', 'angular'], function (app, angular) {
    'use strict';

    app.controller('TrunkController', [
        'pipelineDataService',
        '$scope',
        '$location',
        '$state',
        'pipelineContextService',
        TrunkController
    ]);

    function TrunkController(pipelineDataService, $scope, $location, $state, pipelineContextService) {

        var self = this;
        pipelineContextService.setBranchType('TRUNK');
        self.context = pipelineContextService.context;
        self.module = $state.params.module || self.context.module;
        pipelineContextService.setModule(self.module);
        self.showLoadMoreBuildsLoader = false;
        self.noMoreBuildsToLoad = false;
        self.initBuildsDone = false;
        self.initBuilds = pipelineDataService.getPipelineBuilds(self.module, "TRUNK")
            .then(function (data) {
                self.pipelineBuilds = data instanceof Array ? data : [];
                self.initBuildsDone = true;
            });
        var offset = 15;
        var limit = 15;
        self.loadMoreBuilds = function () {
            self.showLoadMoreBuildsLoader = true;
            pipelineDataService.getPipelineBuildsPaging(self.module, "TRUNK", offset, limit)
                .then(function (data) {
                    if (data instanceof Array && data.length > 0) {
                        angular.forEach(data, function (data) {
                            self.pipelineBuilds.push(data);
                        });
                    }
                    else {
                        self.noMoreBuildsToLoad = true;
                    }
                    self.showLoadMoreBuildsLoader = false;
                });
            offset += limit;
        };
    }
});