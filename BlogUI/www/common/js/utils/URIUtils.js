(function (exports) {
    exports.customEncodeURI = function (uri) {
        uri = encodeURI(uri);
        return uri.replace(/\+/g, '%2B');
    }
    exports.customDecodeURI = function (uri) {
        uri.replace(/(?:%2B)+/g, '\\')
        return decodeURI(uri);
    }
})(this);