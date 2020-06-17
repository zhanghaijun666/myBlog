define(['knockout', "text!./show.html", "css!./show.css"], function (ko, pageView) {

    ko.components.register('personal-resume', {require: '/ui/resource/visitor/personal-resume/main.js'});

    function RouteVisitorModel(params, componentInfo) {
        var self = this;
        BaseComponent.call(self, params, componentInfo);
    }

    RouteVisitorModel.prototype.getRouteParams = function () {
        return {
            baseRoute: "#visitor",
            routes: [
                new RouteItem({
                    baseRoute: "#visitor",
                    route: "/personal",
                    template: 'personal-resume',
                    iconText: "个人简历"
                }),
            ]
        };
    };
    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new RouteVisitorModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
