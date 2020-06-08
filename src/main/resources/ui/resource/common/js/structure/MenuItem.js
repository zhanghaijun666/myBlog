(function (exports) {
    function getValue(value, options) {
        if (typeof visible === "undefined") {
            return true;
        }
        if (ko.isObservable(value)) {
            return ko.unwrap(value);
        }
        if (isFunction(value)) {
            value.call(this, options);
        }
        return value;
    }

    function MenuItem(options) {
        options = options || {};
        var self = $.extend(this, options);
        self.context = options.context;
        self.visible = options.visible;
        self.enable = options.enable;
        self.icon = options.icon;
        self.iconText = options.iconText;
        self.childMenuItems = options.childMenuItems || [];
        self.itemClass = options.itemClass || "";
        self.targetItem = options.targetItem || {};
        self.click = options.click;
    }

    MenuItem.prototype.isVisible = function () {
        return getValue.call(this.context, this.visible, this.targetItem);
    };
    MenuItem.prototype.isEnable = function () {
        return getValue.call(this.context, this.enable, this.targetItem);
    };
    MenuItem.prototype.getChildItems = function () {
        if (this.childMenuItems instanceof Array) {
            return this.childMenuItems;
        } else if (isFunction(this.childMenuItems)) {
            return this.childMenuItems.call(this.context, this.targetItem)
        } else {
            new Array();
        }
    };
    MenuItem.prototype.isDropdown = function () {
        return this.getChildItems().length > 0;
    };
    MenuItem.prototype.menuClick = function () {
        if (isFunction(this.click) && this.targetItem) {
            this.click.call(this.context || this, this.targetItem);
        }
    };

    function RouteItem(options) {
        options = options || {};
        this.baseRoute = options.baseRoute || "";
        this.template = options.template || "";
        this.route = options.route || "";
        MenuItem.call(this, options);
    }

    RouteItem.prototype = new MenuItem();
    RouteItem.prototype.menuClick = function () {
        window.location.hash = FileUrl.join(this.baseRoute, this.route);
    };

    exports.MenuItem = MenuItem;
    exports.RouteItem = RouteItem;
})(this);