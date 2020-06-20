(function (exports) {
    if (!exports.API) {
        exports.API = {};
    }
    exports.API.UserApi = {
        getLoginUser: function (callback) {
            getBinary("/user/token", {cmd: 'GET', Accept: "application/x-protobuf"}, function (data) {
                var user = new Message("UserItem", data);
                callback(user.userId ? user : undefined);
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
        , getAllUser: function () {
            getBinary("/user/all", {cmd: 'GET', Accept: "application/x-protobuf"}, function (data) {
                var array = [];
                ko.utils.arrayForEach(createAutoStructure("UserList", data).items, function (item) {
                    array.push(new Message("UserItem", data,true));
                });
                console.log(array);
                console.log(new Message("UserList", data));
            });
        }
    };
})(this);