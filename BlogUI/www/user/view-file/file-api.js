define(["knockout"], function (ko) {
    return {
        getFile: function (filePath, callback) {
            getBinary("/api/file/" + filePath, {}, callback);
        },
        addFolder: function (filePath, newName) {
            getBinary("/api/file/addfolder/" + newName + "/" + filePath, {cmd: "PUT"}, function (data) {
                console.log(BlogStore.Result.decode(data));
            })
        },
        deleteFile: function (filePath) {
            getBinary("/api/file/" + filePath, {cmd: "DELETE"}, function (data) {
                console.log(BlogStore.Result.decode(data));
            })
        },
        renameFile: function (filePath, newName) {
            getBinary("/api/file/rename/" + newName + "/" + filePath, {cmd: "POST"}, function (data) {
                console.log(BlogStore.Result.decode(data));
            })
        },
        uploadFile: function (files, filePath, callback) {
            if (!(files instanceof FileList) || files.length === 0) {
                console.log("没有文件");
                return;
            }
            const data = new FormData();
            for (let i = 0; i < files.length; i++) {
                data.append("file", files[i]);
            }
            getBinary("/file/upload/" + filePath, {cmd: "POST", data: data}, callback)
        }
    }
});
