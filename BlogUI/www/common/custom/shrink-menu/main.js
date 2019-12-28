(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function ShrinkMenuModel(params, componentInfo) {
            var self = this;
            self.menuClass = params.menuClass || "";
            self.menuList = params.menuList || new Array();
        }
        // nav收缩展开
        ShrinkMenuModel.prototype.itemShrink = function (element, menu) {
            if (!menu.isDropdown()) {
                return;
            }
            if (!$('.menu-shrink').hasClass('nav-mini')) {
                if ($(element).next().css('display') == "none") {
                    //展开未展开
                    $('.nav-item').children('ul').slideUp(300);
                    $(element).next('ul').slideDown(300);
                    $(element).parent('li').addClass('nav-show').siblings('li').removeClass('nav-show');
                } else {
                    //收缩已展开
                    $(element).next('ul').slideUp(300);
                    $('.nav-item.nav-show').removeClass('nav-show');
                }
            }
        };
        //nav-mini切换
        ShrinkMenuModel.prototype.miniShrink = function (element) {
            if (!$('.menu-shrink').hasClass('nav-mini')) {
                $('.nav-item.nav-show').removeClass('nav-show');
                $('.nav-item').children('ul').removeAttr('style');
                $('.menu-shrink').addClass('nav-mini');
                $(".layout-admin>div:nth-child(2)").css('padding-left','70px');
            } else {
                $('.menu-shrink').removeClass('nav-mini');
                $(".layout-admin>div:nth-child(2)").css('padding-left','220px');
            }
        };
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new ShrinkMenuModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
