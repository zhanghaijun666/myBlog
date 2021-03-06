(function (global) {
    define(['knockout', "text!./show.html", "css!./show.css"], function (ko, pageView) {
        function CustomCardViewModel(params, componentInfo) {
            var defaultValue = {
                dataList: []  /*数据*/
                , headTemplate: undefined
                , rowTemplate: ""
                , noDataHint: '无数据提示'
                , context: {}
                , rightMenus: null
                , topMenus: null
                , noDataImgCss: ""
                , loading: false
            };
            var self = $.extend(this, defaultValue, params);
            BaseComponent.call(self, params, componentInfo);
            if (ko.isObservable(self.loading)) {
                self.loading.extend({rateLimit: 50});
            }
            self.chosenItems = ko.observableArray();
            //https://www.tutorialsplane.com/knockoutjs-select-unselect-all-checkbox-list/
            self.chosenAll = ko.pureComputed({
                read: function () {
                    return this.chosenItems().length > 0 && this.chosenItems().length === ko.unwrap(this.dataList).length;
                },
                write: function (value) {
                    this.chosenItems(value ? ko.unwrap(this.dataList).slice(0) : []);
                },
                owner: self
            });
        }

        CustomCardViewModel.prototype.getChosenOrAll = function () {
            return this.chosenItems().length + "/" + ko.unwrap(this.dataList).length;
        };
        CustomCardViewModel.prototype.isShowTopMenu = function () {
            return this.getMenus(this.topMenus).length > 0;
        };
        CustomCardViewModel.prototype.getTopMenuParams = function () {
            return {
                name: "custom-menu",
                data: {
                    origin: this.context,
                    count: -1,
                    menuList: this.getMenus(this.topMenus)
                }
            }
        };

        CustomCardViewModel.prototype.isShowHead = function () {
            return typeof this.headTemplate !== "undefined";
        };
        CustomCardViewModel.prototype.getHeadParams = function () {
            return {
                name: "custom-card-head",
                data: {
                    headTemplate: {
                        name: this.headTemplate,
                        data: {}
                    }
                }
            }
        };
        CustomCardViewModel.prototype.getBodyParams = function () {
            return {
                name: ko.unwrap(this.loading) ? "custom-card-loading" : "custom-card-body",
                data: {
                    context: this.context,
                    dataList: this.dataList,
                    rowTemplate: this.rowTemplate,
                    rightMenu: function (data) {
                        return {
                            name: "custom-menu",
                            data: {
                                menuList: this.getMenus(this.rightMenus, data),
                                count: 4,
                            }
                        }
                    }.bind(this)
                }
            }
        };
        CustomCardViewModel.prototype.getMenus = function (menus, rowData) {
            if (menus instanceof Array) {
                return menus;
            } else if (isFunction(menus)) {
                return menus.bind(this.context, rowData)();
            } else {
                return new Array();
            }
        };
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
