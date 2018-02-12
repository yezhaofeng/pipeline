/**
 * Created by Wonpang New on 2016/9/10.
 *
 * app service
 */

define(['app', 'constants'], function (app, constants) {
    app.service(
        'pipelineDataService',
        [
            '$http',
            '$q',
            PipelineService
        ]
    );

    function PipelineService($http, $q) {
        var self = this;

        // 搜索模块名
        self.getSearchModules = function (searchVal, username) {
            return $http.get(constants.api('/pipeline/module/query?limit=70&q=' + searchVal + '&owner=' + username))
                .then(function (data) {
                    return data.data;
                });
        };

        /**
         * 获得主干流水线
         * @param username
         * @param module
         * @param pipelineBuildId
         * @returns {*}
         */
        self.getTrunkPipelines = function (username, module, pipelineBuildId) {
            return $http.get(constants.api('pipeline/v1/pipelineBuilds?username=' + username + '&module=' + module
                    + '&pipelineBuildId=' + pipelineBuildId))
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         * 获取流水线配置信息
         * @param module
         * @param branchType
         */
        self.getPipelineConf = function (module, branchType) {
            return $http.get(constants.api('pipeline/conf/' + module + '/' + branchType))
                .then(function (data) {
                    return data.data;
                })
        };
        /**
         * 添加Jenkins配置
         * @param jenkinsConf
         * @returns {*}
         */
        self.addJenkinsConf = function (jenkinsConf) {
            return $http.post(constants.api('pipeline/jenkins/server'), jenkinsConf)
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         *
         * 获取发布记录
         * @param module
         * @param releaseId
         * @returns {*}
         */
        self.getReleaseHistory = function (module, releaseId) {
            return $http.get(constants.api('pipeline/release/' + module))
                .then(function (data) {
                    return data.data;
                });
        };

        /**
         * 获取Jenkins配置信息
         * @returns {*}
         */
        self.getJenkinsConfs = function () {
            return $http.get(constants.api('pipeline/jenkins/server/all'))
                .then(function (data) {
                    return data.data;
                });
        };

        self.deleteJenkinsConf = function (id) {
            return $http.delete(constants.api("pipeline/jenkins/server/" + id))
                .then(function (data) {
                    return data.data;
                })
        };

        /**
         * 增加新的模块
         * @param username
         * @param repository
         * @returns {*}
         */
        self.addModule = function (username, repository) {
            return $http.get(constants.api('github/addModule?owner=' + username + '&repository=' + repository))
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         * 当前登录用户信息
         * @returns {*}
         */
        self.getLoginUserInfo = function () {
            return $http.get(constants.api("pipeline/user/current")).then(function (data) {
                return data.data;
            })
        };
        /**
         * 获取插件信息
         * @returns {*}
         */
        self.getPluginInfo = function () {
            return $http.get(constants.api("plugin/job/configs")).then(function (data) {
                return data.data;
            })
        };

    }
});
