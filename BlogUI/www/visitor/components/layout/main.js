(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function LayoutDemoModel(params, componentInfo) {
            var self = this;

        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new LayoutDemoModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
