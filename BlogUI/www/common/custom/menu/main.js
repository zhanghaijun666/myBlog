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

        MenuListModel.prototype.getTemplate = function () {
            if (this.menuClass === "shrink-menu") {
                return "menu-shrink-template";
            }
            return "menu-normal-template";
        };
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
        // nav收缩展开
        MenuListModel.prototype.itemShrink = function (element, menu) {
            if (!menu.isDropdown()) {
                return;
            }
            if (!$('.nav').hasClass('nav-mini')) {
                if ($(element).next().css('display') == "none") {
                    //展开未展开
                    $('.nav-item').children('ul').slideUp(300);
                    $(element).next('ul').slideDown(300);
                    $(element).parent('li').addClass('nav-show').siblings('li').removeClass('nav-show');
                }else{
                    //收缩已展开
                    $(element).next('ul').slideUp(300);
                    $('.nav-item.nav-show').removeClass('nav-show');
                }
            }
        };
        //nav-mini切换
        MenuListModel.prototype.miniShrink = function (element) {
            if (!$('.nav').hasClass('nav-mini')) {
                $('.nav-item.nav-show').removeClass('nav-show');
                $('.nav-item').children('ul').removeAttr('style');
                $('.nav').addClass('nav-mini');
            } else {
                $('.nav').removeClass('nav-mini');
            }
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
