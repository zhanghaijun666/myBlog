(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function UserMessageModel(params, componentInfo) {
            let self = this;
            self.stompClient = Stomp.over(new SockJS("/websocket")),
                self.stompClient.connect({}, function (frame) {
                    console.log(frame);
                    self.stompClient.subscribe('/topic/notice', function (message) {
                        console.log(message);
                    });
                    self.stompClient.subscribe('/topic/getResponse', function (message) {
                        console.log(message);
                    });
                    self.stompClient.send("/topic/notice",{},"6666666666666");
                });
            params.disposecallback = function () {
                self.stompClient.disconnect()
            };
            window.stompClient = self.stompClient;
            BaseComponent.call(self, params, componentInfo);
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
