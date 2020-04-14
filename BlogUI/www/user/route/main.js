define(['knockout', "text!./show.html", "css!./show.css"], function (ko, pageView) {

    function RouteUserModel(params, componentInfo) {
        var self = this;
        BaseComponent.call(self, params, componentInfo);
    }

    RouteUserModel.prototype.getContainerRoute = function (baseRoute) {
        return [];
    };
    RouteUserModel.prototype.getLeftParams = function () {
        return {
            name: "user-brief",
            data: [
                new MenuItem({icon: "fa fa-address-card", iconText: "我的菜单", ItemClass: "", targetItem: {}}),
                new MenuItem({icon: "fa fa-address-card", iconText: "我的菜单", ItemClass: "", targetItem: {}}),
                new MenuItem({icon: "fa fa-address-card", iconText: "我的菜单", ItemClass: "", targetItem: {}})
            ]
        };
    };
    RouteUserModel.prototype.getHeadParams = function () {
        return {
            name: "user-navbar",
            data: {
                menus: [
                    new MenuItem({icon: "fa fa-home fa-lg", iconText: "欢迎登录", ItemClass: "", targetItem: {}}),
                    new MenuItem({icon: "fa fa-sign-out", iconText: "退出", ItemClass: "", targetItem: {}})
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
