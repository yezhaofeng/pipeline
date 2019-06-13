/**
 * Created by Wonpang New on 2019/9/10.
 *
 * app service
 */

define(['app', 'constants'], function (app, constants) {
    app.service (
        'permissionService',
        [
            '$http',
            '$q',
            PermissionService
        ]
    );

    function PermissionService($http, $q) {
        var self = this;

        self.loginSystem = function (username, password) {
            return $http.get(constants.api('cihome/login/loginSystem?username='
                + username + '&password=' + password))
                .then(function (data) {
                    return data.data;
            });
        };

        self.exitLogin = function (username) {
            return $http.get(constants.api('cihome/login/exitLogin?username=' + username))
                .then(function (data) {
                    return data.data;
                });
        };
    }
})
