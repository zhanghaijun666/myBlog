(function (exports) {
    function MenuItem(name, options){
        var self = $.extend(this,options);
        self.name = name;
        self.visible = options.visible;
        self.disable = typeof options.disable !== "undefined" ? options.disable : function () {
            if (typeof options.visible === "undefined") {
                return false; //没有设置visible和disable, 没有添加权限
            }
            var visible = typeof options.visible === "function" ? options.visible.bind(self.targetItem || this)() : options.visible;
            return !visible;
        };
        self.icon = options.icon;
        self.iconText = options.iconText;
        self.childMenuItems = options.childMenuItems || [];
        self.ItemClass = options.ItemClass || "";
        self.targetItem =  options.targetItem || {};
    }
    MenuItem.prototype.getPermit = function () {
        if (isFunction(this.disable)) {
            return !this.disable.bind(this.targetItem || this)();
        }
        return !this.disable;
    };
    MenuItem.prototype.isDropdown = function () {
        return this.childMenuItems && this.childMenuItems.length > 0;
    };


    exports.MenuItem = MenuItem;
})(this);