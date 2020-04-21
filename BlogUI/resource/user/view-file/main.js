define(["text!./show.html", "./file-api.js", "./file-utils.js", "css!./show.css"], function (pageView, FileAPI, FileUtil) {
    function ViewFileModel(params, componentInfo) {
        const self = $.extend(this, FileUtil);
        BaseComponent.call(self, params, componentInfo);
        self.dataList = ko.observableArray([]);
        self.parentUrl = BlogStore.OwnerType.User + "/" + RootView.loginUser().userId + "/default";


        self.getFileList();
    }

    ViewFileModel.prototype.getFileList = function () {
        var context = this;
        FileAPI.getFile(context.parentUrl, function (data) {
            var storeList = BlogStore.StoreFile.StoreList.decode(data);
            var array = [];
            for (var i = 0; i < storeList.items.length; i++) {
                array.push(new Message("StoreFile.StoreTree", storeList.items[i]));
            }
            // console.log(storeList.parentItem);
            context.dataList(array);
        });
    };
    ViewFileModel.prototype.uploadFile = function () {
        FileAPI.uploadFile(document.getElementById("upload-file").files, this.parentUrl, this.getFileList.bind(this));
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
                targetItem: data,
                icon: "fa fa-remove",
                iconText: '删除',
                itemClass: 'btn btn-default',
                click: function (data) {
                    console.log(data);
                }
            }),
            new MenuItem({
                targetItem: data,
                icon: "fa fa-wrench",
                iconText: '重命名',
                itemClass: 'btn btn-default',
                click: function (data) {
                    console.log(data);
                }
            })
        ];
    };
    ViewFileModel.prototype.getTopMenus = function () {
        return [
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
