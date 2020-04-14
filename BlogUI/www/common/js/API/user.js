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
        , login: function (username, password) {
            var data = new FormData();
            data.append("username", username);
            data.append("password", password);
            getBinary("/login", {cmd: 'POST', data: data, type: "application/x-www-form-urlencoded"}, function (data) {
                console.log(data);
                // location.reload();
            });
        }
    };
})(this);