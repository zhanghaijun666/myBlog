(function (global) {
    define(['knockout', "text!./show.html"], function (ko, pageView) {
        //http://stackoverflow.com/questions/17983118/change-observable-but-dont-notify-subscribers-in-knockout-js
        ko.observable.fn.withPausing = function () {
            this.notifySubscribers = function () {
                if (!this.pauseNotifications) {
                    ko.subscribable.fn.notifySubscribers.apply(this, arguments);
                }
            };
            this.sneakyUpdate = function (newValue) {
                this.pauseNotifications = true;
                this(newValue);
                this.pauseNotifications = false;
            };
            return this;
        };
        //http://stackoverflow.com/questions/17983118/change-observable-but-dont-notify-subscribers-in-knockout-js
        ko.observable.fn.withPausing = function () {
            this.notifySubscribers = function () {
                if (!this.pauseNotifications) {
                    ko.subscribable.fn.notifySubscribers.apply(this, arguments);
                }
            };
            this.sneakyUpdate = function (newValue) {
                this.pauseNotifications = true;
                this(newValue);
                this.pauseNotifications = false;
            };

            return this;
        };

        function KoRouteModel(params, componentInfo) {
            var self = this;
            BaseComponent.call(self, params, componentInfo);
            self.baseRoute = params.baseRoute;
            self.routes = params.routes || new Array();
            self.templateName = ko.observable("").withPausing();
            self.templateData = ko.observable({}).withPausing();
            self.switchRoute.call(self, RootView.routeUrl);
            self.watch(RootView.routeUrl, self.switchRoute.bind(self));
        }

        KoRouteModel.prototype.getTemplateParams = function () {
            return {name: this.templateName(), data: this.templateData};
        };
        KoRouteModel.prototype.switchRoute = function (value) {
            const routeItem = this.getCurrentRouteItem(ko.unwrap(value));
            if (routeItem && ko.unwrap(this.templateName) == routeItem.item.template) {
                this.templateData().routePath(routeItem.path);
            } else if (routeItem) {
                this.templateName.sneakyUpdate(routeItem.item.template);
                this.templateData.sneakyUpdate({
                    routeBase: this.baseRoute,
                    route: routeItem.item,
                    routePath: ko.observable(routeItem.path)
                });
            } else {
                this.templateName.sneakyUpdate("route-404");
                this.templateData.sneakyUpdate({});
            }
        };
        KoRouteModel.prototype.getCurrentRouteItem = function (hash) {
            for (var i = 0; i < this.routes.length; i++) {
                let item = this.routes[i];
                if (!item.template) {
                    continue;
                }
                let currentPath = hash;
                if (hash && this.baseRoute && hash.indexOf(this.baseRoute) === 0) {
                    currentPath = hash.substring(this.baseRoute.length)
                }
                if (!item.route) {
                    return {item: item, path: currentPath};
                }
                if (!item.route || (currentPath && currentPath.indexOf(item.route) === 0)) {
                    return {item: item, path: currentPath.substring(item.route.length)};
                }
            }
            return undefined;
        };

        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new KoRouteModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
