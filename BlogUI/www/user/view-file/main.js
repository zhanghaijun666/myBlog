define(["text!./show.html", "css!./show.css"], function (pageView) {
    function ViewFileModel(params, componentInfo) {
        const self = this;
        BaseComponent.call(self, params, componentInfo);
    }

    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new ViewFileModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
