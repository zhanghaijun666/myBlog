define(["knockout"], function (ko) {
    return {
        renderSize: function (value) {
            if (null == value || value == '') {
                return "0 Bytes";
            }
            const unitArr = new Array("Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB");
            let index = 0,
                srcsize = parseFloat(value);
            index = Math.floor(Math.log(srcsize) / Math.log(1024));
            let size = srcsize / Math.pow(1024, index);
            size = size.toFixed(2);
            return size + unitArr[index];
        },
        openFile: function (data) {
            if (data instanceof Message && data.storeName === "StoreFile.StoreTree" && data.storeType === BlogStore.StoreFile.StoreTypeEnum.Tree) {
                window.location.hash = FileUrl.join(this.routeBase, this.getParentFullPath(), data.fileName);
            }
        }
    }
});
