define(["text!./show.html", "css!./show.css"], function (pageView) {
    function UserListModel(params, componentInfo) {
        var self = this;

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
