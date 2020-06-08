//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {
    //初始界面
    ko.components.register('login-page', {require: '/ui/resource/visitor/login-page/main.js'});
    ko.components.register('route-visitor', {require: '/ui/resource/visitor/route/main.js'});
    ko.components.register('route-user', {require: '/ui/resource/user/route/main.js'});
    ko.components.register('route-admin', {require: '/ui/resource/admin/route/main.js'});

    //公共组件
    ko.components.register('ko-route', {require: '/ui/resource/common/custom/ko-route/main.js'});
    ko.components.register('drop-down', {require: '/ui/resource/common/custom/dropdown/main.js'});
    ko.components.register('custom-menu', {require: '/ui/resource/common/custom/menu/main.js'});
    ko.components.register('custom-card', {require: '/ui/resource/common/custom/card/main.js'});
    ko.components.register('custom-tree', {require: '/ui/resource/common/custom/tree/main.js'});
    ko.components.register('head-image', {require: '/ui/resource/common/custom/head-image/main.js'});
    ko.components.register('entry', {require: '/ui/resource/common/custom/entry/main.js'});


    //特效组件
    ko.components.register('canvas-three', {require: '/ui/resource/visitor/canvas/three/main.js'});
    ko.components.register('bongo-cat', {require: '/ui/resource/visitor/plugin/bongo-cat/main.js'});

    ko.components.register('route-404', {require: '/ui/resource/other/route-404/main.js'});
    ko.components.register('page-404', {require: '/ui/resource/other/page-404/main.js'});

    //系统管理组件
})(this, ko);
