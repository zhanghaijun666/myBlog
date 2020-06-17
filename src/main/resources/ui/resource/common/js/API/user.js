(function (exports) {
    if (!exports.API) {
        exports.API = {};
    }
    exports.API.UserApi = {
        getLoginUser: function (callback) {
            getBinary("/user/login", {cmd: 'GET', Accept: "application/json"}, function (data) {
                var user = JSON.parse(data);
                callback(user.username ? user : undefined);
            }, function () {
                callback(undefined);
            });
        }
        , logout: function () {
            getBinary("/logout", {cmd: 'GET', Accept: "application/json"}, function () {
                location.reload();
            });
        }
        , login: function (username, password, callback) {
            var data = new FormData();
            if (!username || !password) {
                return;
            }
            data.append("username", username);
            data.append("password", password);
            data.append("remember-me", "true");
            getBinary("/login", {cmd: 'POST', data: data, Accept: "application/json"}, callback);
        }
    };
})(this);