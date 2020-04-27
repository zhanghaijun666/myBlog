define(["text!./show.html", "./file-api.js", "./file-utils.js", "css!./show.css"], function (pageView, FileAPI, FileUtil) {
    function ViewFileModel(params, componentInfo) {
        params = params || {};
        const self = $.extend(this, FileUtil);
        BaseComponent.call(self, params, componentInfo);
        self.routeBase = params.routeBase;
        self.loading = ko.observable(false);
        self.dataList = ko.observableArray([]);
        self.parentUrl = ko.observable(self.getFileUrl(params.routePath));
        self.watch(params.routePath, function (path) {
            self.parentUrl(self.getFileUrl(path));
            self.getFileList();
        });
        let fileUpload = document.getElementById("uploadFile");
        fileUpload.addEventListener("change", function () {
            FileAPI.uploadFile(this.files, self.getParentFullPath(), self.getFileList.bind(self));
        });
        self.getFileList();
    }

    ViewFileModel.prototype.getFileUrl = function (path) {
        return FileUrl.decodeFileUrlPath(ko.unwrap(path)) || new FileUrl(BlogStore.OwnerType.User, RootView.loginUser().userId);
    };
    ViewFileModel.prototype.getParentFullPath = function () {
        return ko.unwrap(this.parentUrl).getFullPath();
    };
    ViewFileModel.prototype.getFileList = function () {
        const context = this;
        context.loading(true);
        FileAPI.getFile(context.getParentFullPath(), function (data) {
            context.loading(false);
            var storeList = BlogStore.StoreFile.StoreList.decode(data);
            var array = [];
            for (var i = 0; i < storeList.items.length; i++) {
                array.push(new Message("StoreFile.StoreTree", storeList.items[i]));
            }
            array.sort(function (v1, v2) {
                const storeType = v1.storeType - v2.storeType;
                return 0 === storeType ? v1.fileName.localeCompare(v2.fileName, "zh") : storeType;
            });
            // console.log(storeList.parentItem);
            context.dataList(array);
        });
    };
    ViewFileModel.prototype.getFileCardParams = function () {
        return {
            name: "custom-card",
            data: {
                context: this,
                loading: this.loading,
                headTemplate: "file-header-template",
                rowTemplate: "row-file-template",
                dataList: this.dataList,
                rightMenus: this.getRightMenuParams,
                topMenus: this.getTopMenus
            }
        }
    };
    ViewFileModel.prototype.getEntryParams = function () {
        return {
            name: "entry",
            data: {
                url: this.parentUrl,
                routeBase: this.routeBase,
                refresh: this.getFileList.bind(this),
                loadind: this.loading
            }
        }
    };
    ViewFileModel.prototype.getRightMenuParams = function (data) {
        return [
            new MenuItem({
                context: this,
                targetItem: data,
                icon: "fa fa-download",
                iconText: '下载',
                click: function (data) {
                    FileAPI.downloadFile(FileUrl.join(this.getParentFullPath(), data.fileName));
                }
            }),
            new MenuItem({
                context: this,
                targetItem: data,
                icon: "fa fa-remove",
                iconText: '删除',
                click: function (data) {
                    FileAPI.deleteFile(this.getParentFullPath() + data.fileName, this.getFileList.bind(this));
                }
            }),
            new MenuItem({
                context: this,
                targetItem: data,
                icon: "fa fa-wrench",
                iconText: '重命名',
                click: function (data) {
                    const context = this;
                    showInputDailog({
                        success: function (dialog, container) {
                            FileAPI.renameFile(FileUrl.join(context.getParentFullPath(), data.fileName), ko.unwrap(dialog.inputValue), context.getFileList.bind(context));
                        }
                    });
                }
            })
        ];
    };
    ViewFileModel.prototype.getTopMenus = function () {
        return [
            new MenuItem({
                context: this,
                icon: "fa fa-upload",
                iconText: '上传',
                childMenuItems: [
                    new MenuItem({
                        iconText: '上传文件',
                        click: function () {
                            document.getElementById('uploadFile').click();
                        }
                    })
                ]
            }),
            new MenuItem({
                context: this,
                icon: "fa fa-folder-open-o",
                iconText: '新建文件夹',
                click: function () {
                    const context = this;
                    showInputDailog({
                        success: function (dialog, container) {
                            FileAPI.addFolder(context.getParentFullPath(), ko.unwrap(dialog.inputValue), context.getFileList.bind(context));
                        }
                    });
                }
            })
        ]
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
