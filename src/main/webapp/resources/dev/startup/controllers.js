/**
 * Created by Wonpang New on 2019/9/10.
 *
 * controllers 入口
 */

define(
    [
        'js/pipeline/controllers/index-controller',
        'js/pipeline/controllers/default-controller',
        'js/pipeline/controllers/trunk-controller',
        'js/pipeline/controllers/branches-controller',
        'js/pipeline/controllers/branch-controller',
        'js/pipeline/controllers/builds-controller',
        'js/permission/controllers/permission-controller',
        'js/permission/controllers/register-controller',
        'js/pipeline/controllers/release-history-controller',
        'js/pipeline/controllers/github-controller',
        'js/pipeline/controllers/jenkins-controller',
        'js/pipeline/controllers/config-controller',

        'resources/dev/plugin/job/type/COMPILE/main.js',
        'resources/dev/plugin/job/type/RELEASE/main.js',
        'resources/dev/plugin/job/type/JENKINS_JOB/main.js'

    ], function () {
    });
