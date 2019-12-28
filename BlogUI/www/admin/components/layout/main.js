define(["text!./show.html", "css!./show.css"], function (pageView) {
    function LayoutAdminModel(params, componentInfo) {
        var self = this;

    }
    LayoutAdminModel.prototype.getAdminRoutes = function () {
        return [
            {route:"",template:"user-list"},
            {route:"",template:"bongo-cat"},
            {route:"",template:"canvas-three"}
        ];
    };
    LayoutAdminModel.prototype.getAdminBar = function () {
        return [
            new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}}),
            new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}}),
            new MenuItem({
                visible: true, icon: 'fa fa-address-book', iconText: '管理用户下拉', targetItem: {}, childMenuItems: [
                    new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}}),
                    new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}}),
                    new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}})
                ]
            }),
            new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}}),
            new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}}),
            new MenuItem({
                visible: true, icon: 'fa fa-address-book', iconText: '管理用户下拉', targetItem: {}, childMenuItems: [
                    new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}}),
                    new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}}),
                    new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}})
                ]
            }),
            new MenuItem({visible: true, icon: 'fa fa-address-book', iconText: '管理用户', targetItem: {}})
        ];
    };
    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new LayoutAdminModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
