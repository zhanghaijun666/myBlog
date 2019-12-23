(function (global) {
    define(['knockout', "text!./show.html","css!./show.css"], function (ko,pageView) {
        function CustomCardViewModel(params, componentInfo) {
            var defaultValue = {
                dataList: []  /*数据*/
                , headTemplateLarge: "custom-card-body-head"
                , rowTemplateLarge: ""
                , noDataHint: l10n("operate_hint.empty")  /*无数据提示*/
                , context: {}
                , rightMenus: null
                , topMenus: null
                , noDataImgCss: ""
                , loading: ko.observable(false)           
            };
            var self = $.extend(this, defaultValue, params);
            
            BaseComponent.call(self, params, componentInfo);
 

        }
        CustomCardViewModel.prototype.getMenus = function (Menus, origin) {
            if (Menus instanceof Array) {
                return Menus;
            } else if (isFunction(Menus)) {
                return updateFeatureArray(Menus.bind(this.context,origin)());
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
        $.extend(CustomCardViewModel.prototype, Selector.prototype);

        return {
            createViewModel: function (params, componentInfo) {
                return new CustomCardViewModel(params, componentInfo);
            },
            template: pageView
        };

    });
})(this);
