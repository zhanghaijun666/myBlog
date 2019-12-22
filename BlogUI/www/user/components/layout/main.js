(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function LayoutUserModel(params, componentInfo) {
            var self = this;

        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new LayoutUserModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
