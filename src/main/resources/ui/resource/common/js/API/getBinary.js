(function (exports) {
    exports.HEADER_STRING = "Authorization";
    exports.getBinary = function (url, options, callback, errorCallback) {
        if (!url) {
            return;
        }
        url = url.replace(/\/+/g, "/")
        var ot;
        var oloaded = 0;
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
        xhr.upload.onloadstart = function () {//上传开始执行方法
            ot = new Date().getTime();
            oloaded = 0;//设置上传开始时，以上传的文件//设置上传开始时间大小为0
        };
        //【上传进度调用方法实现】
        xhr.upload.onprogress = function (evt) {
            //https://www.cnblogs.com/tianyuchen/p/5594641.html
            console.log(Math.round(evt.loaded / evt.total * 100) + "%");

            var pertime = (new Date().getTime() - ot) / 1000; //计算出上次调用该方法时到现在的时间差，单位为s
            ot = new Date().getTime(); //重新赋值时间，用于下次计算

            var perload = evt.loaded - oloaded; //计算该分段上传的文件大小，单位b
            oloaded = evt.loaded;//重新赋值已上传文件大小，用以下次计算

            //上传速度计算
            var speed = perload / pertime;//单位b/s
            var bspeed = speed;
            var units = 'b/s';//单位名称
            if (speed / 1024 > 1) {
                speed = speed / 1024;
                units = 'k/s';
            }
            if (speed / 1024 > 1) {
                speed = speed / 1024;
                units = 'M/s';
            }
            speed = speed.toFixed(1);
            const resttime = ((evt.total - evt.loaded) / bspeed).toFixed(1);
            console.log('速度：' + speed + units + '，剩余时间：' + resttime + 's');
            if (bspeed == 0) {
                console.log('上传已取消');
            }
            // setTimeout(function () {
            //     xhr.abort(); //取消上传
            // },1000)
        };

        xhr.timeout = 0; // 设置超时时间,0表示永不超时
        xhr.open(options.cmd || 'GET', url, true);
        if (options.data instanceof FormData) {
            xhr.send(options.data);
            return;
        }
        //接受的数据类型
        const accept = options.Accept || (/^\/?api\/.*/.test(url) ? 'application/x-protobuf' : 'text/plan');
        xhr.setRequestHeader('Accept', accept);
        xhr.setRequestHeader(HEADER_STRING, localStorage.getItem(HEADER_STRING));
        xhr.responseType = accept === "application/x-protobuf" ? "arraybuffer" : (options.responseType || "text");
        if (options.data) {
            xhr.setRequestHeader("Content-Type", options.type || (/^\/?api\/.*/.test(url) ? 'application/x-protobuf' : 'application/json'));
            xhr.send(options.data);
        } else {
            xhr.send();
        }
    };
})(this);