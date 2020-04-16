define(["knockout"], function (ko) {
    return {
        uploadFile: function (files) {
            if (!(files instanceof FileList) || files.length === 0) {
                console.log("没有文件");
                return;
            }
            const data = new FormData();
            for (let i = 0; i < files.length; i++) {
                data.append("file", files[i]);
            }
            getBinary("/api/file/upload", {cmd: "POST", data: data,}, function (data) {
                console.log(data);
            })
        }
    }
});
