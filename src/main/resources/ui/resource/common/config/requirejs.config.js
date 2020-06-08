requirejs.config({
    //By default load any module IDs from js/lib
    baseUrl: "",
    //except, if the module ID starts with "app",
    //load it from the js/app directory. paths
    //config is relative to the baseUrl, and
    //never includes a ".js" extension since
    //the paths config could be for a directory.
    paths: {
        "text": "/ui/static/require/text",
        "css": "/ui/static/require/css",
        "BlogStore": "/ui/resource/common/config/blog-store"
    },
    shim: {
    }
});

define("knockout", [], function () {
    return ko;
});

define("protobufjs/light", [], function () {
    return protobuf;
});