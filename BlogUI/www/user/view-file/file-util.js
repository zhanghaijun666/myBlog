define(["knockout"], function (ko) {
    return {
        getFile: function (filePath) {
            getBinary("/api/file/get/" + filePath, {}, function (data) {
                console.log(BlogStore.FileStore.StoreTree.decode(data));
            })
        },
        uploadFile: function (files, filePath) {
            if (!(files instanceof FileList) || files.length === 0) {
                console.log("没有文件");
                return;
            }
            const data = new FormData();
            for (let i = 0; i < files.length; i++) {
                data.append("file", files[i]);
            }
            getBinary("/file/upload/" + filePath, {cmd: "POST", data: data}, function (data) {
                console.log(data);
            })
        }
    }
});
