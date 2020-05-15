(function (exports) {
    function BaseComponent(params, componentInfo) {
        params = params || {};
        var self = this;
        var subscriptions = new Array();

        if (params.ref) {   /*ref 是外部使用的钩子， 外部调用内部的commponent的参数， 可以往ref 里进行传参*/
            params.ref(self);
        }

        self.watch = function (subscribable, subscriber, callbackTarget) {
            if (ko.isObservable(subscribable)) {
                subscriptions.push(subscribable.subscribe(subscriber, callbackTarget));
            }
        };

        //component销毁时自动调用
        self.dispose = function () {
            subscriptions.forEach(function (subscription) {
                subscription.dispose();
            });
            if (isFunction(params.disposecallback)) {
                params.disposecallback();
            }
        };
    }

    exports.BaseComponent = BaseComponent;
})(this);