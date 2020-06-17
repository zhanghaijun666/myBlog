(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function LoginModel(params, componentInfo) {
            var self = this;
            self.username = ko.observable("");
            self.password = ko.observable("");
            self.errorStr = ko.observable();

        }

        LoginModel.prototype.doLogin = function () {
            const context = this;
            API.UserApi.login(context.username(), context.password(), function (data) {
                var result = JSON.parse(data);
                if (!result.code || result.code == 1) {
                    localStorage.setItem(HEADER_STRING, result.result);
                    RootView.sammyPage.refresh();
                } else {
                    context.errorStr(result.msg);
                }
            });
        };
        LoginModel.prototype.gotoResetPassword = function () {

        };
        LoginModel.prototype.gotoRegister = function () {

        };
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new LoginModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
