(function (global) {
    define(['knockout',"text!./show.html", "css!./show.css"], function (ko,pageView) {
        function LayoutUserModel(params, componentInfo) {
            var self = this;

        }
        LayoutUserModel.prototype.getUserMenu = function () {
            return [
                new MenuItem("",{icon:"fa fa-address-card",iconText:"我的菜单",ItemClass:"",targetItem:{}}),
                new MenuItem("",{icon:"fa fa-address-card",iconText:"我的菜单",ItemClass:"",targetItem:{}}),
                new MenuItem("",{icon:"fa fa-address-card",iconText:"我的菜单",ItemClass:"",targetItem:{}})
            ];
        };
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new LayoutUserModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
