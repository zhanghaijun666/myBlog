(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function Page404Model(params, componentInfo) {
        }

        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new Page404Model(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
