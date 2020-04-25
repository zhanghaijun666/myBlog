(function (exports) {
    function FileUrl(ownerType, ownerId, alias, path) {
        this.ownerType = ownerType || BlogStore.OwnerType.User;
        this.ownerId = ownerId || 0;
        this.alias = alias || "default";
        this.path = path || "/";
    }

    FileUrl.prototype.getPathPrefix = function () {
        return FileUrl.join(this.ownerType, this.ownerId, this.alias);
    };
    FileUrl.prototype.getFullPath = function () {
        return FileUrl.join(this.ownerType, this.ownerId, this.alias, this.path);
    };

    FileUrl.decodeFileUrlPath = function (url) {
        if (!url) {
            return undefined;
        }
        url = url.replace(/^\/+/, "");
        let segments = url.match(/\/?(\d+)\/(\d+)\/([^/]+)(\/?.*)/);
        if (segments !== null) {
            return new FileUrl(segments[1], segments[2], segments[3], segments[4])
        }
        return undefined;
    };

    FileUrl.join = function () {
        return ("/" + Array.prototype.slice.call(arguments).join("/")).replace(/\/+/g, "/");
    };

    exports.FileUrl = FileUrl;
})(this);