(function (global) {
    define(['knockout', "text!./show.html", "css!./show.css"], function (ko, pageView) {
        function MenuListModel(params, componentInfo) {
            var self = this;
            self.menuClass = params.menuClass || "right-menu"; //shrink-menu
            self.menuList = params.menuList || new Array();
            self.count = isNaN(params.count) ? 4 : Number(params.count);
            self.permitType = params.permitType || "visible";
            self.shrinkData = null;
            if (self.menuClass === "shrink-menu") {//收缩菜单
                self.shrinkParams = {
                    shrinkLarge: params.shrinkLarge || ko.observable(true),
                    template: params.template || "",
                    templateData: params.templateData || {}
                }
            }
        }

        MenuListModel.prototype.getMenuList = function () {
            this.shrinkLarge(!this.shrinkLarge());
        }
        MenuListModel.prototype.getMenuList = function () {
            if (this.permitType !== "visible") {
                return ko.unwrap(this.menuList);
            }
            var list = new Array();
            ko.unwrap(this.menuList).forEach(function (item) {
                if (item.getPermit()) {
                    list.push(item);
                }
            });
            return list;
        };
        MenuListModel.prototype.getMenuWith = function () {
            var list = this.getMenuList();
            if (this.count > 0 && list.length > this.count) {
                return {
                    menus: list.slice(0, this.count - 1),
                    dropMenu: new MenuItem({
                        visible: true,
                        icon: 'fa fa-bars',
                        iconText: '更多',
                        childMenuItems: list.slice(this.count - 1, list.length)
                    })
                };
            }
            return {menus: list};
        };
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new MenuListModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
