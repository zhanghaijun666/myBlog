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
            }
            var iconObj = this.getFileIcon();
            for (var i = 0; i < Object.keys(iconObj).length; i++) {
                var icon = Object.keys(iconObj)[i];
                if (iconObj[icon].indexOf(this.data.contentType) !== -1) {
                    return "/static/images/file/" + icon + ".png";
                }
            }
            // console.log(this.data.contentType);
            // console.log(this.data.fileName);
            return "/static/images/file/other.png";
        }
        return "/static/images/file/other.png";
    };
    HeadImageModel.prototype.getFileIcon = function () {
        return {
            "doc": ["text/xml", "application/msword",],
            "image": ["image/png", "image/gif", "application/x-img", "image/jpeg"],
            "music": ["audio/mp3"],
            "video": ["video/x-mpg", "video/x-mpeg", "video/mpg", "video/mp4"],
            "pdf": ["application/pdf"],
            "ppt": ["application/vnd.ms-powerpoint"],
            "txt": ["text/plain", "text/xml"],
            "xls": ["application/x-xls", "application/vnd.ms-excel"],
            "zip": []
        }
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
