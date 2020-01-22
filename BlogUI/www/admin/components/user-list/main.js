define(["text!./show.html", "css!./show.css"], function (pageView) {
    function UserListModel(params, componentInfo) {
        const self = this;
        self.userList = ko.observableArray([]);
        self.getAllUser();
    }


    UserListModel.prototype.getTopMenu = function () {
        return [
            new MenuItem({
                visible: true,
                iconText: '新建用户',
                itemClass: 'btn btn-primary',
                click: this.addUser.bind(this)
            })
        ]
    };
    UserListModel.prototype.getRightMenus = function () {
        return [
            new MenuItem({
                visible: true,
                iconText: '删除用户',
                itemClass: '',
                click: this.addUser.bind(this)
            }),
            new MenuItem({
                visible: true,
                iconText: '编辑用户',
                itemClass: '',
                click: this.addUser.bind(this)
            })
        ]
    };
    UserListModel.prototype.getAllUser = function () {
        const context = this;
        getBinary("/api/user/all/" + false, {cmd: 'GET'}, function (data) {
            context.userList(BlogStore.UserList.decode(data).items);
        });
    };
    UserListModel.prototype.addUser = function () {
        const message = new Message("UserItem", null, true);
        const num = Math.floor(Math.random() * 10);
        message.username(num + "name");
        message.nickname(num + "username");
        message.email(num + "email@163.com");
        message.phone(num + "1234567");
        message.birthday(953481600000);
        getBinary("/api/user", {cmd: 'POST', data: message.toArrayBuffer()}, function (data) {
            console.log(BlogStore.Result.decode(data));
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
