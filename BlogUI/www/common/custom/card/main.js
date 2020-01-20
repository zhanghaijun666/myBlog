(function (global) {
    define(['knockout', "text!./packed-card.html","css!./show.css"], function (ko,pageView) {
        function CustomCardViewModel(params, componentInfo) {
            var defaultValue = {
                dataList: []  /*数据*/
                , headTemplateLarge: "custom-card-body-head"
                , rowTemplateLarge: ""
                , noDataHint: '无数据提示'
                , context: {}
                , rightMenus: null
                , topMenus: null
                , noDataImgCss: ""
                , loading: ko.observable(false)           
            };
            var self = $.extend(this, defaultValue, params);
            
            BaseComponent.call(self, params, componentInfo);
 

        }
        CustomCardViewModel.prototype.getMenus = function (menus, origin) {
            if (menus instanceof Array) {
                return Menus;
            } else if (isFunction(menus)) {
                return menus.bind(this.context,origin)();
            } else {
                return new Array();
            }
        };
        CustomCardViewModel.prototype.getRightMenus = function (origin) {
            return this.getMenus(this.rightMenus, origin);
        };
        CustomCardViewModel.prototype.getTopMenus = function (origin) {
            return this.getMenus(this.topMenus, origin);
        };
        // $.extend(CustomCardViewModel.prototype, Selector.prototype);

        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new CustomCardViewModel(params, componentInfo);
                }
            },
            template: pageView
        };

    });
})(this);
