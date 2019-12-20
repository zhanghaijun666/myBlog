requirejs([], function (bcstore) {
    function RootViewModel() {
        var self = this;
        self.user = "haijun";





    }
    window.RootView = new RootViewModel();
    ko.applyBindings(window.RootView);
});
