(function (global) {
    function ToastRoot(options) {
        const self = this;
        options = options || {};
        self.returnCode = options.returnCode;
        self.message = options.message || "未知提示";
        self.headText = options.headText || "提示信息";
    }

    ToastRoot.prototype.getMessage = function () {
        if (this.returnCode && BlogStore.lookupEnum("ReturnCode").valuesById[this.returnCode]) {
            return l10n('returnCode.' + BlogStore.lookupEnum("ReturnCode").valuesById[this.returnCode]);
        } else {
            return this.message;
        }
    };
    global.ToastRoot = ToastRoot;
    global.toastShow = function (options) {
        const toast = new ToastRoot(options);
        let list = null;
        if ($("body>.toast-list").length > 0) {
            list = ko.dataFor($("body>.toast-list")[0]).list;
        } else {
            const container = document.createElement("div");
            container.className = "toast-list";
            container.innerHTML = "<!-- {{{ --><!-- ko template: 'toast-list-template' --><!-- }}} --><!-- {{{ --><!-- /ko --><!-- }}} -->";
            document.body.appendChild(container);
            list = ko.observableArray([]);
            ko.applyBindings({list: list}, container);
        }
        list.push(toast);
        window.setTimeout(function (toastList, item) {
            toastList.remove(item);
        }.bind(this, list, toast), 1500);
    };
    global.toastShowMsg = function (Msg) {
        toastShow({message: Msg});
    };
    global.toastShowCode = function (code) {
        if(code && !isNaN(code)){
            toastShow({returnCode: code});
        }else if(code instanceof BlogStore.Result.ctor){
            toastShow({returnCode: code.code});
        }else if(code instanceof BlogStore.results.ctor){

        }
    };



})(this);
