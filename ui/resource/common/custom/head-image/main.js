define(["text!./show.html", "css!./show.css"], function (pageView) {
    function HeadImageModel(params, componentInfo) {
        const self = this;
        BaseComponent.call(self, params, componentInfo);
        self.data = params.data;

    }

    HeadImageModel.prototype.getImgSrc = function () {
        if (!this.data) {
            return "/static/images/file/other.png";
        }
        if (this.data instanceof Message && this.data.storeName === "StoreFile.StoreTree") {
            if (!this.data.contentType) {
                return "/static/images/file/folder.png";
            } else if (this.data.icon) {
                return "/static/images/file/" + this.data.icon + ".png";
            }
        }
        return "/static/images/file/other.png";
    };
    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new HeadImageModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
