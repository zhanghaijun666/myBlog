(function (exports) {
    function FileData(options) {
        options = options || {};
        const self = this;
        self.name = options.name;
        self.fullPath = options.fullPath;
        self.contentType = options.contentType;
        self.size = options.size;
        self.createdTime = options.createdTime;
        self.updateTime = options.updateTime;
    }

    exports.FileData = FileData;
})(this);