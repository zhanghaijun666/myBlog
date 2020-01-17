(function (exports) {
    exports.getBinary = function (url, options, callback) {
        if (!url) {
            return;
        }
        options = options || {};
        var xhr;
        if (window.XMLHttpRequest) {
            xhr = new XMLHttpRequest();
        } else {
            // code for IE6, IE5
            xhr = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xhr.onload = function () {
            switch (xhr.status) {
                case 200:
                    if (isFunction(callback)) {
                        callback(xhr.response || xhr.responseText, xhr);
                    }
                    return;
                default:
                    xhr.onerror();
                    return;
            }
        };
        xhr.onloadend = function () {
            // 请求结束
        };
        xhr.onerror = function () {
            // 请求出错
        };
        xhr.ontimeout = function () {
            // 请求超时
        };
        xhr.timeout = 0; // 设置超时时间,0表示永不超时
        xhr.open(options.cmd || 'GET', url, true);
        //接受的数据类型
        xhr.setRequestHeader('Accept', options.Accept || (/^\/?api\/.*/.test(url) ? 'application/x-protobuf' : 'text/plan'));
        if (options.data) {
            xhr.setRequestHeader("Content-Type", options.type || "application/octet-stream");
            xhr.send(options.data);
        } else {
            xhr.send();
        }
    };
})(this);