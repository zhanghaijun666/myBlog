define(["text!./show.html", "./file-api.js", "./file-utils.js", "css!./show.css"], function (pageView, FileAPI, FileUtil) {
    function ViewFileModel(params, componentInfo) {
        const self = $.extend(this, FileUtil);
        BaseComponent.call(self, params, componentInfo);
        self.dataList = ko.observableArray([]);
        self.parentUrl = BlogStore.OwnerType.User + "/" + RootView.loginUser().userId + "/default/";


        let fileUpload = document.getElementById("uploadFile");
        fileUpload.addEventListener("change", function () {
            FileAPI.uploadFile(this.files, self.parentUrl, self.getFileList.bind(self));
        });
        self.getFileList();
    }

    ViewFileModel.prototype.getFileList = function () {
        const context = this;
        FileAPI.getFile(context.parentUrl, function (data) {
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
                rowTemplate: "row-file-template",
                dataList: this.dataList,
                rightMenus: this.getRightMenuParams,
                topMenus: this.getTopMenus
            }
        }
    };
    ViewFileModel.prototype.getRightMenuParams = function (data) {
        return [
            new MenuItem({
                context: this,
                targetItem: data,
                icon: "fa fa-remove",
                iconText: '删除',
                itemClass: 'btn btn-default',
                click: function (data) {
                    FileAPI.deleteFile(this.parentUrl + data.fileName, this.getFileList.bind(this));
                }
            }),
            new MenuItem({
                context: this,
                targetItem: data,
                icon: "fa fa-wrench",
                iconText: '重命名',
                itemClass: 'btn btn-default',
                click: function (data) {
                    const context = this;
                    showInputDailog({
                        success: function (dialog, container) {
                            FileAPI.renameFile(context.parentUrl + data.fileName, ko.unwrap(dialog.inputValue), context.getFileList.bind(context));
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
                itemClass: 'btn btn-primary',
                childMenuItems: [
                    new MenuItem({
                        icon: "fa fa-upload",
                        iconText: '上传文件',
                        itemClass: 'btn btn-primary',
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
                itemClass: 'btn btn-primary',
                click: function () {
                    const context = this;
                    showInputDailog({
                        success: function (dialog, container) {
                            FileAPI.addFolder(context.parentUrl, ko.unwrap(dialog.inputValue), context.getFileList.bind(context));
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
