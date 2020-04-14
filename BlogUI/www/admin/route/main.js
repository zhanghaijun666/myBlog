define(['knockout', "text!./show.html", "css!./show.css"], function (ko, pageView) {

    ko.components.register('admin-navbar', {require: '/static/www/admin/shrink-menu/main.js'});
    ko.components.register('user-list', {require: '/static/www/admin/user-list/main.js'});

    function RouteAdminModel(params, componentInfo) {
        var self = this;
        BaseComponent.call(self, params, componentInfo);
    }

    RouteAdminModel.prototype.getContainerRoute = function (baseRoute) {
        return [
            new RouteItem({template: 'user-list'})
        ];
    };
    RouteAdminModel.prototype.getLeftParams = function () {
        return {
            name: "admin-navbar",
            data: {
                menuClass: 'shrink-menu', menuList: this.getAdminBar()
            }
        }
    };
    RouteAdminModel.prototype.getAdminBar = function () {
        return [
            new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理首页', targetItem: {}}),
            new MenuItem({
                visible: true, icon: 'fa fa-address-book', iconText: '系统配置', targetItem: {}, childMenuItems: [
                    new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}})
                ]
            })
        ];
    };
    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new RouteAdminModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
