(function (global) {
    define(["text!./show.html", "./socket.js", "css!./show.css"], function (pageView, BlogSocket) {
        function UserMessageModel(params, componentInfo) {
            let self = this;
            BaseComponent.call(self, params, componentInfo);
            self.socket = new BlogSocket();
            setTimeout(function () {
                self.socket.send("你们好");
            }, 1000)


        }

        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new UserMessageModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
