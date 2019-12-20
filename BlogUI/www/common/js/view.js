requirejs([], function (bcstore) {
    function RootViewModel() {
        var self = this;

    }
    window.RootView = new RootViewModel();
    ko.applyBindings(window.RootView,window.document.body);
});
