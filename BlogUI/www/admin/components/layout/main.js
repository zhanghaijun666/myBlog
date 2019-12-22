(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function LayoutAdminModel(params, componentInfo) {
            var self = this;

        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new LayoutAdminModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
