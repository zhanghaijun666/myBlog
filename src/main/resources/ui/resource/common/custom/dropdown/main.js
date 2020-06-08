(function (global) {
    define(['knockout',"text!./show.html", "css!./show.css"], function (ko,pageView) {
        function DropDownModel(params, componentInfo) {
            var self = this;
            self.downClass = params.downClass || "";
            self.item = params.item || new MenuItem();
            self.context = params.context || {};

        }
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new DropDownModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
