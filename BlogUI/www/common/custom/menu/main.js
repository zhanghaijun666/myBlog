(function (global) {
    define(['knockout', "css!./show.css"], function (ko) {
        function MenuListModel(params, componentInfo) {
            var self = this;
            BaseComponent.call(self, params, componentInfo);
            self.menuClass = params.menuClass || "right-menu";
            self.menuList = params.menuList || new Array();
            self.count = isNaN(params.count) ? 4 : Number(params.count);
            self.permitType = params.permitType || "visible";

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
            var list = updateFeatureArray(this.getMenuList());
            if (this.count > 0 && list.length > this.count) {
                return {menus: list.slice(0, this.count - 1), dropMenu: list.slice(this.count - 1, list.length)};
            }
            return {menus: list, dropMenu: new Array()};
        };
        return {
            createViewModel: function (params, componentInfo) {
                return new MenuListModel(params, componentInfo);
            }
        };
    });
})(this);
