(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function LoginModel(params, componentInfo) {
            var self = this;

        }
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
