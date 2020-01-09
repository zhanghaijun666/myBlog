requirejs(["knockout","BlogStore"], function (ko,BlogStore) {
    window.BlogStore = BlogStore;
    function RootViewModel() {
        var self = this;
        self.rootTemplate = ko.observable("layout-visitor");
        self.routeHash = ko.observable("");
        self.sammyPage = new SammyPage({view: self}); /*thie last step*/
    }

    RootViewModel.prototype.setRootTemplate = function (template) {
        if (this.rootTemplate() !== template) {
            this.rootTemplate(template);
        }
    };
    window.RootView = new RootViewModel();
    ko.applyBindings(window.RootView, window.document.body);
});
