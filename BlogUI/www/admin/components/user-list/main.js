define(["text!./show.html", "css!./show.css"], function (pageView) {
    function UserListModel(params, componentInfo) {
        const self = this;
        self.userList = ko.observableArray([]);
        self.getAllUser();
    }

    UserListModel.prototype.getAllUser = function () {
        const context = this;
        getBinary("/api/user/all/" + false, {cmd: 'GET'}, function (data) {
            context.userList(BlogStore.UserList.decode(data).items);
        });
    };
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
    UserListModel.prototype.addUser = function () {

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
