define(["text!./show.html", "css!./show.css"], function (pageView) {
    function UserListModel(params, componentInfo) {
        var self = this;

        self.getAllUser();
    }

    UserListModel.prototype.getAllUser = function () {
        getBinary("/api/user/all/" + false, {cmd: 'GET', Accept: "application/json"}, function (data) {
            console.log(data);
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
