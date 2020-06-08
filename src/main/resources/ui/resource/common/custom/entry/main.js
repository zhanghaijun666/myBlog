(function (global) {
    define(['knockout', "text!./show.html", "css!./show.css"], function (ko, pageView) {
        function EntryModel(params, componentInfo) {
            let self = this;
            BaseComponent.call(self, params, componentInfo);
            self.routeBase = params.routeBase;
            self.refresh = params.refresh;
            self.url = params.url; //FileUrl
            self.urlArray = ko.observableArray(self.getUrlArray(self.url));
            if (ko.isObservable(self.url)) {
                self.watch(self.url, function (value) {
                    self.urlArray(self.getUrlArray(value));
                });
            }
        }

        EntryModel.prototype.jumpPath = function (item) {
            if (typeof item === "string") {
                window.location.hash = FileUrl.join(this.routeBase, item).substring(1);
                return;
            } else if (item instanceof FileUrl) {
                this.jumpPath(item.getFullPath());
            } else if (item && typeof item.path === "string") {
                this.jumpPath(item.path);
            } else {
                this.jumpPath("");
            }
        };
        EntryModel.prototype.previous = function () {
            let array = this.getUrlArray(this.url);
            array.pop();
            const item = array.pop();
            window.location.hash = FileUrl.join(this.routeBase, item ? item.path : "").substring(1);
        };
        EntryModel.prototype.refreshEvent = function () {
            if (isFunction(this.refresh)) {
                this.refresh();
            }
        };
        EntryModel.prototype.getUrlArray = function (pathUrl) {
            if (ko.isObservable(pathUrl)) {
                return this.getUrlArray(ko.unwrap(pathUrl));
            } else if (typeof pathUrl === "string") {
                return this.getUrlArray(FileUrl.decodeFileUrlPath(pathUrl));
            } else if (pathUrl instanceof FileUrl) {
                const prefix = pathUrl.getPathPrefix();
                var routeArray = [];
                pathUrl.path.split("/").forEach(function (item, index, arr) {
                    if (item) {
                        routeArray.push({name: item, path: FileUrl.join(prefix, arr.slice(0, index + 1).join("/"))});
                    }
                });
                return routeArray;
            }
            return [];
        };
        EntryModel.prototype.isRootUrl = function () {
            return ko.unwrap(this.urlArray).length === 0;
        };

        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new EntryModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
