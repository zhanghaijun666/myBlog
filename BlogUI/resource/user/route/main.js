define(['knockout', "text!./show.html", "css!./show.css"], function (ko, pageView) {

    ko.components.register('view-file', {require: '/resource/user/view-file/main.js'});
    ko.components.register('message', {require: '/resource/user/message/main.js'});

    function RouteUserModel(params, componentInfo) {
        var self = this;
        BaseComponent.call(self, params, componentInfo);
    }

    RouteUserModel.prototype.getRouteParams = function (baseRoute) {
        return {
            baseRoute: "#user",
            routes: [
                new RouteItem({route: "/file", template: 'view-file'}),
                new RouteItem({route: "/message", template: 'message'})
            ]
        };
    };
    RouteUserModel.prototype.getLeftParams = function () {
        return {
            name: "user-brief",
            data: [
                new MenuItem({
                    icon: "fa fa-cog fa-lg", iconText: "我的文件", ItemClass: "", targetItem: {}, click: function () {
                        window.location.hash = "#user/file";
                    }
                }),
                new MenuItem({
                    icon: "fa fa-cog fa-lg", iconText: "消息聊天", ItemClass: "", targetItem: {}, click: function () {
                        window.location.hash = "#user/message";
                    }
                }),
                new MenuItem({icon: "fa fa-cog fa-lg", iconText: "个人设置", ItemClass: "", targetItem: {}})
            ]
        };
    };
    RouteUserModel.prototype.getHeadParams = function () {
        return {
            name: "user-navbar",
            data: {
                menus: [
                    new MenuItem({
                        icon: "fa fa-home fa-lg",
                        iconText: RootView.loginUser() ? RootView.loginUser().name : "未知",
                        ItemClass: "",
                        targetItem: {},
                        visible: RootView.loginUser()
                    }),
                    new MenuItem({
                        icon: "fa fa-sign-out",
                        iconText: "退出",
                        ItemClass: "",
                        targetItem: {},
                        click: API.UserApi.logout
                    })
                ]
            }
        };
    };

    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new RouteUserModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
