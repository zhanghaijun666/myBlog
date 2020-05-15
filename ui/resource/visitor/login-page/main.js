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
                context.errorStr(data);
                if (!data) {
                    RootView.sammyPage.refresh();
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
