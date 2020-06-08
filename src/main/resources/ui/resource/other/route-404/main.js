(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function Route404Model(params, componentInfo) {

        }

        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new Route404Model(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
