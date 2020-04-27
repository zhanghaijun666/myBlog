(function (global) {
    define(['knockout', "text!./show.html", "css!./show.css", "css!./top-nav.css", "css!./vertical-nav.css", "css!./horizontal.css"], function (ko, pageView) {
        function MenuListModel(params, componentInfo) {
            var self = this;
            self.menuClass = params.menuClass || "horizontal";
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
                if (item.isVisible()) {
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
