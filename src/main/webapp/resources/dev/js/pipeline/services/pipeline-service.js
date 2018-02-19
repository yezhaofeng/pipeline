/**
 * Created by Wonpang New on 2016/9/10.
 *
 * app service
 */

define(['app', 'angular', 'constants'], function (app, angular, constants) {
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
        // 获取commit信息
        self.getCommits = function (module, branchType) {
            return $http.get(constants.api('github/' + module + '/' + branchType + "/commits"))
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         * 获得流水线构建信息
         * @param module
         * @param branchType
         * @returns {*}
         */
        self.getPipelineBuilds = function (module, branchType) {
            return $http.get(constants.api('pipeline/build/' + module + '/' + branchType))
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         * 获得流水线构建信息
         * @param module
         * @param branchType
         * @param branch
         * @returns {*}
         */
        self.getPipelineBuildsByBranch = function (module, branchType, branch) {
            return $http.get(constants.api('pipeline/build/' + module + '/' + branchType + "/" + branch))
                .then(function (data) {
                    return data.data;
                });
        };

        /**
         * 开启默认流水线
         * @param module
         */
        self.openDefaultPipelineConf = function (module) {
            return $http.post(constants.api('pipeline/conf/' + module))
                .then(function (data) {
                    return data.data;
                })
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
         * 保存修改的流水线配置信息
         * @param module
         * @param branchType
         * @param config
         */
        self.savePipelineConf = function (module, branchType, config) {
            return $http.put(constants.api('pipeline/conf/' + module + '/' + branchType), config)
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
         * 验证Jenkins配置
         * @param jenkinsConf
         * @returns {*}
         */
        self.checkJenkinsConf = function (jenkinsConf) {
            return $http.post(constants.api('pipeline/jenkins/server/isActive'), jenkinsConf)
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
         * 获取Job的运行时依赖
         * @param id
         */
        self.getJobRuntimeRequire = function (id) {
            return $http.get(constants.api('pipeline/job/runtimeRequire/' + id))
                .then(function (data) {
                    return data.data;
                });
        };


        /**
         * Job构建信息
         * @param id
         * @param param
         * @returns {*}
         */
        self.getJobBuild = function (id) {
            return $http.get(constants.api('/pipeline/job/' + id))
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         * 获取模块下所有的分支（不含master）
         * @param module
         * @returns {*}
         */
        self.getBranches = function (module) {
            return $http.get(constants.api('pipeline/' + module + '/branches'))
                .then(function (data) {
                    return data.data;
                });
        };

        /**
         * 手动构建Job
         * @param id
         * @param param
         * @returns {*}
         */
        self.doJobBuild = function (id, param) {
            return $http.post(constants.api('/pipeline/job/' + id), param)
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         * 手动构建流水线
         * @param pipelineConfId 流水线id
         * @param triggerId 若为0，则拿最新的commit进行构建
         * @returns {*}
         */
        self.buildPipelineByConfId = function (pipelineConfId, triggerId) {
            return $http.post(constants.api('pipeline/build?pipelineConfId=' + pipelineConfId + '&triggerId=' + triggerId))
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         * 手动构建流水线
         * @param module
         * @param branchType
         * @param triggerId 若为0，则拿最新的commit进行构建
         * @returns {*}
         */
        self.buildPipeline = function (module, branchType, triggerId) {
            return $http.post(constants.api('pipeline/build/' + module + '/' + branchType + '?triggerId=' + triggerId))
                .then(function (data) {
                    return data.data;
                });
        };
        /**
         * 获取Jenkins配置信息
         * @returns {*}
         */
        self.getJenkinsConfs = function (username) {
            return $http.get(constants.api('pipeline/jenkins/server/all?owner=' + username))
                .then(function (data) {
                    return data.data;
                });
        };

        /**
         * 删除Jenkins配置
         * @param id
         * @returns {*}
         */
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
        self.getPluginInfos = function () {
            return $http.get(constants.api("plugin/job/configs")).then(function (data) {
                return data.data;
            })
        };

        /**
         * 根据插件类型获取插件信息
         * @returns {*}
         */
        self.getPluginInfoByType = function (pluginType) {
            return $http.get(constants.api("plugin/job/configs")).then(function (data) {
                var plugins = data.data;
                for (var x in plugins) {
                    if (plugins[x].pluginType == pluginType) {
                        return plugins[x];
                        break;
                    }
                    return null;
                }
            })
        };
        /**
         * 获取用户下所有的JenkinsJob信息
         * @param username
         * @returns {*}
         */
        self.getJenkinsJobs = function (username) {
            return $http.get(constants.api("pipeline/jenkins/server/jobs?owner=" + username)).then(function (data) {
                return data.data;
            })
        };
    }
});

