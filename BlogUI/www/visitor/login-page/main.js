(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function LoginModel(params, componentInfo) {
            var self = this;

        }
        LoginModel.prototype.gotoLogin = function () {

        };
        LoginModel.prototype.gotoResetPassword = function () {

        };
        LoginModel.prototype.gotoRegister = function () {
            getBinary("/api/user/test",{cmd:'GET',Accept:"application/json"},function(data){
                console.log(data);
            });
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
