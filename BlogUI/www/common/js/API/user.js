(function (exports) {
    if (!exports.API) {
        exports.API = {};
    }
    exports.API.UserApi = {
        getLoginUser: function (callback) {
            getBinary("/login-user", {cmd: 'GET'}, function (data) {
                var user = JSON.parse(data);
                callback(user.name ? user : undefined);
            }, function () {
                callback(undefined);
            });
        }
        , logout: function () {
            getBinary("/logout", {cmd: 'GET'}, function () {
                location.reload();
            });
        }
    };
})(this);