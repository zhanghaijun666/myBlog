requirejs(["knockout", "BlogStore"], function (ko, BlogStore) {
    window.BlogStore = BlogStore;

    function RootViewModel() {
        var self = this;
        self.rootTemplate = ko.observable("blog-loading-page");
        self.routeUrl = ko.observable("");
        self.loginUser = ko.observable();
        self.sammyPage = new SammyPage({view: self}); /*thie last step*/
    }

    RootViewModel.prototype.setRootTemplate = function (template) {
        if (this.rootTemplate() !== template) {
            this.rootTemplate(template);
        }
    };
    RootViewModel.prototype.getLoginUser = function (callback) {
        if (!isFunction(callback)) {
            return;
        }
        const context = this;
        if (context.loginUser()) {
            callback(context.loginUser());
        } else {
            API.UserApi.getLoginUser(function (user) {
                context.loginUser(user);
                callback(context.loginUser());
            });
        }
    };
    window.RootView = new RootViewModel();
    ko.applyBindings(window.RootView, window.document.body);
});
