define(["text!./show.html", "css!./show.css"], function (pageView) {
    function UserListModel(params, componentInfo) {
        var self = this;

        self.getAllUser();
    }

    UserListModel.prototype.getAllUser = function () {
        getBinary("/api/user/all/" + false, {cmd: 'GET', Accept: "application/x-protobuf"}, function (data) {
            console.log(data);
            console.log(BlogStore.UserList.decode(data));
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
