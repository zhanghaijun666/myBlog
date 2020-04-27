define(["knockout"], function (ko) {
    function BlogSocket() {
        if (!("WebSocket" in window)) {
            alert("您的浏览器不支持 WebSocket!");
        }
        let self = this;
        self.connected = ko.observable(false);
        self.blogSocket = new WebSocket((/^https/.test(location.protocol) ? "wss://" : "ws://") + window.location.host + "/blogsocket/" + RootView.loginUser().userId);
        self.blogSocket.onopen = function (event) {
            self.connected(true);
        };
        self.blogSocket.onmessage = function (event) {
            console.log(event.data + "webSocket 消息");
        };
        self.blogSocket.onclose = function (event) {
            self.connected(false);
            // console.log("webSocket 连接已关闭,{}", event);
        };
        self.blogSocket.onerror = function (event) {
            self.connected(false);
            console.log("webSocket 异常,{}", event);
        };
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            self.blogSocket.close();
        };
    }

    BlogSocket.prototype.send = function (message) {
        if (this.connected()) {
            this.blogSocket.send(message);
        }
    };
    return BlogSocket
});
