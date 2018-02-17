/**
 * Created by Wonpang New on 2016/9/10.
 *
 */

define(['app'], function (app) {
    'use strict';

    app.controller('BuildsController', [
        '$scope',
        '$location',
        '$state',
        'pipelineContextService',
        'pipelineDataService',
        BuildsController
    ]);

    function BuildsController($scope, $location, $state, pipelineContextService,pipelineDataService) {
        var self = this;
        self.context = pipelineContextService.context;
        $scope.buildPipeline = function(){
            var module = self.context.module;
            var branchType = self.context.branchType;
            if(branchType === 'TRUNK'){
            pipelineDataService.buildPipeline(module, branchType,0).then(function(response){
                if (response.success == true) {
                    alert("任务提交成功");
                } else {
                    alert(response.message);
                }
            });
            }else if(branchType === 'BRANCH'){
                // TODO
                console.log("未实现")
            }
        };
    }
});