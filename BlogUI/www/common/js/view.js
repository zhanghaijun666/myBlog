requirejs(["knockout"], function (ko) {
    function RootViewModel() {
        var self = this;
        self.rootTemplate = ko.observable("layout-visitor");

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
