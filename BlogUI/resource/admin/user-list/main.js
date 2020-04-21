define(["text!./show.html", "css!./show.css"], function (pageView) {
    function UserListModel(params, componentInfo) {
        const self = this;
        BaseComponent.call(self, params, componentInfo);
        self.userList = ko.observableArray([]);
        self.getAllUser();
    }


    UserListModel.prototype.getTopMenu = function () {
        return [
            new MenuItem({
                visible: true,
                icon: "fa fa-plus-square",
                iconText: '新建用户',
                itemClass: 'btn btn-primary',
                click: this.addUser.bind(this)
            })
        ]
    };
    UserListModel.prototype.getRightMenus = function (data) {
        return [
            new MenuItem({
                visible: true,
                icon: "fa fa-times",
                iconText: '删除用户',
                itemClass: '',
                targetItem: data,
                click: this.deleteUser.bind(this)
            }),
            new MenuItem({
                visible: true,
                icon: "fa fa-edit",
                iconText: '编辑用户',
                itemClass: '',
                targetItem: data,
                click: this.addUser.bind(this)
            })
        ]
    };
    UserListModel.prototype.getStatusStr = function (status) {
        switch (status) {
            case BlogStore.Status.StatusActive:
                return "有效";
            case BlogStore.Status.StatusDeleted:
                return "删除";
            default:
                return "未知";
        }
    };
    UserListModel.prototype.getAllUser = function () {
        const context = this;
        getBinary("/api/user/all/" + false, {cmd: 'GET'}, function (data) {
            const userArray = BlogStore.UserList.decode(data).items;
            context.userList(userArray.sort(function (a, b) {
                return a.username.localeCompare(b.username, 'zh-CN')
            }));
        });
    };
    UserListModel.prototype.addUser = function () {
        const context = this;
        const message = new Message("UserItem", null, true);
        const num = Math.floor(Math.random() * 10);
        message.username(num + "name");
        message.nickname(num + "username");
        message.email(num + "email@163.com");
        message.phone(num + "1234567");
        message.birthday(953481600000);
        getBinary("/api/user", {cmd: 'POST', data: message.toArrayBuffer()}, function (data) {
            toastShowCode(BlogStore.Result.decode(data));
            context.getAllUser();
        });
    };
    UserListModel.prototype.deleteUser = function (data) {
        if (!data) {
            return;
        }
        const context = this;
        const req = BlogStore.UserList.create();
        req.items.push(data);
        getBinary("/api/user", {cmd: 'DELETE', data: BlogStore.UserList.encode(req).finish()}, function (data) {
            toastShowCode(BlogStore.Result.decode(data));
            context.getAllUser();
        });
    };
    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new UserListModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
