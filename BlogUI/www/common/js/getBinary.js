(function (exports) {
    exports.getBinary = function (url, options, callback, errorCallback) {
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
            if (xhr.readyState == 4 && xhr.status === 200) {
                let result;
                if (xhr.responseType === 'text') {
                    result = xhr.responseText;
                } else if (xhr.responseType === 'document') {
                    result = xhr.responseXML;
                } else if (xhr.responseType === "arraybuffer") {
                    //https://www.jianshu.com/p/5c7825570351
                    result = new Uint8Array(xhr.response);
                } else {
                    result = xhr.response;
                }
                if (isFunction(callback)) {
                    callback(result, xhr);
                }
                return;
            } else {
                xhr.onerror();
                return;
            }
        };
        xhr.onloadend = function () {
            // 请求结束
        };
        xhr.onerror = function () {
            if (xhr.status === 409) {
                toastShowMsg("你的账号在另一地点被登录");
                location.reload();
            } else if (isFunction(errorCallback)) {
                errorCallback(xhr);
            }
        };
        xhr.ontimeout = function () {
            // 请求超时
        };
        xhr.timeout = 0; // 设置超时时间,0表示永不超时
        xhr.open(options.cmd || 'GET', url, true);
        //接受的数据类型
        const accept = options.Accept || (/^\/?api\/.*/.test(url) ? 'application/x-protobuf' : 'text/plan');
        xhr.setRequestHeader('Accept', accept);
        xhr.responseType = accept === "application/x-protobuf" ? "arraybuffer" : (options.responseType || "text");
        if (options.data) {
            xhr.setRequestHeader("Content-Type", options.type || (/^\/?api\/.*/.test(url) ? 'application/x-protobuf' : 'application/json'));
            xhr.send(options.data);
        } else {
            xhr.send();
        }
    };
})(this);