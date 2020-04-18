define(["text!./show.html", "./file-util.js", "css!./show.css"], function (pageView, FileUtil) {
    function ViewFileModel(params, componentInfo) {
        const self = this;
        BaseComponent.call(self, params, componentInfo);
        self.parentUrl = BlogStore.OwnerType.User + "/" + RootView.loginUser().userId + "/default";

        FileUtil.getFile(self.parentUrl);
    }

    ViewFileModel.prototype.uploadFile = function () {
        FileUtil.uploadFile(document.getElementById("upload-file").files, this.parentUrl);
    };

    ViewFileModel.prototype.getTopMenuParams = function () {
        return {
            name: "custom-menu",
            data: {
                count: -1,
                menuList: [
                    new MenuItem({
                        icon: "fa fa-upload",
                        iconText: '上传',
                        itemClass: 'btn btn-primary',
                        childMenuItems: [
                            new MenuItem({
                                icon: "fa fa-upload",
                                iconText: '上传文件',
                                itemClass: 'btn btn-primary'
                            })
                        ]
                    }),
                    new MenuItem({
                        icon: "fa fa-folder-open-o",
                        iconText: '新建文件夹',
                        itemClass: 'btn btn-primary'
                    })
                ]
            }
        }
    };
    ViewFileModel.prototype.getFileCardParams = function () {
        return {
            name: "custom-card",
            data: {
                rowTemplate: "row-file-template",
                dataList: ko.observableArray([
                    new FileData({name: "111.txt"}),
                    new FileData({name: "222.txt"}),
                    new FileData({name: "333.txt"}),
                ])
            }
        }
    };

    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new ViewFileModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
