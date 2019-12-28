(function (global) {
    define(['knockout', "text!./show.html"], function (ko, pageView) {
        function KoRouteModel(params, componentInfo) {
            var self = this;
            BaseComponent.call(self, params, componentInfo);
            self.routes = params.routes || new Array();
            self.currentName = ko.observable();
            if(self.routes.length > 0){
                self.currentName(self.routes[0].template);
            }
            self.watch(RootView.routeHash,function(value){
                console.log(value);
            });
        }
        KoRouteModel.prototype.getTemplateParams = function () {
            return {name: this.currentName(),data:{}};
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
