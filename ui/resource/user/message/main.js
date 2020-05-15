(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function UserMessageModel(params, componentInfo) {
            let self = this;
            self.contactList = ko.observableArray([]);
            self.messageList = ko.observableArray([]);
            self.messagText = ko.observable("");

            self.stompClient = Stomp.over(new SockJS("/blogchat")),
                self.stompClient.connect({
                    username: RootView.loginUser().name,
                    userId: RootView.loginUser().userId
                }, function (frame) {
                    var userName = frame.headers['user-name'];
                    if (!userName) {
                        return;
                    }
                    // 聊天室订阅
                    self.stompClient.subscribe('/topic/chatRoom', function (data) {
                        self.messageList.push(data.body);
                    });
                    // 本地订阅
                    stompClient.subscribe('/user/' + userName + '/chat', function (data) {
                        self.messageList.push(data.body);
                    });
                    // 聊天室动态订阅
                    stompClient.subscribe('/topic/status', function (data) {
                        self.contactList(JSON.parse(JSON.parse(data.body).items).items);
                        bootoast({
                            message: JSON.parse(data.body).online,
                            type: 'success',
                            position: 'bottom-right',
                            timeout: 0.5
                        });
                    });
                }, function (error) {
                    console.log("socket链接异常");
                });
            params.disposecallback = function () {
                self.stompClient.disconnect()
            };
            window.stompClient = self.stompClient;
            BaseComponent.call(self, params, componentInfo);
        }

        UserMessageModel.prototype.sendMessage = function () {
            if (this.messagText() && this.stompClient) {
                // this.stompClient.send('/chatRoom', {}, this.messagText());
                this.stompClient.send('/chat', {}, this.messagText());
                this.messagText("");
                setTimeout(function () {
                    var talk_window = $("body .message div.talk_window > div.windows_body")[0];
                    talk_window.scrollTop = talk_window.scrollHeight;
                }, 100);
            }
        };

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
