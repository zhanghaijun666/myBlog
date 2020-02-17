(function (exports) {
    function MenuItem(options){
        options = options || {};
        var self = $.extend(this,options);
        self.visible = options.visible;
        self.disable = typeof options.disable !== "undefined" ? options.disable : function () {
            var visible = true;
            if (typeof options.visible !== "undefined") {
                visible = typeof options.visible === "function" ? options.visible.bind(self.targetItem || this)() : options.visible;
            }
            return !visible;
        };
        self.icon = options.icon;
        self.iconText = options.iconText;
        self.childMenuItems = options.childMenuItems || [];
        self.itemClass = options.itemClass || "";
        self.targetItem =  options.targetItem || {};
        self.click = options.click || function(){};
    }
    MenuItem.prototype.getPermit = function () {
        if (isFunction(this.disable)) {
            return !this.disable.bind(this.targetItem || this)();
        }
        return !this.disable;
    };
    MenuItem.prototype.getChildItems = function () {
        if(this.childMenuItems instanceof Array){
            return this.childMenuItems;
        }else if(isFunction(this.childMenuItems)){
            return this.childMenuItems();
        }else{
            new Array();
        }
    };
    MenuItem.prototype.isDropdown = function () {
        return this.getChildItems().length > 0;
    };
    MenuItem.prototype.menuClick = function () {
        if(this.targetItem){
            this.click(this.targetItem);
        }
    };


    exports.MenuItem = MenuItem;
})(this);