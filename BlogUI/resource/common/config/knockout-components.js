//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {
    //初始界面
    ko.components.register('login-page', {require: '/resource/visitor/login-page/main.js'});
    ko.components.register('route-visitor', {require: '/resource/visitor/route/main.js'});
    ko.components.register('route-user', {require: '/resource/user/route/main.js'});
    ko.components.register('route-admin', {require: '/resource/admin/route/main.js'});

    //公共组件
    ko.components.register('ko-route', {require: '/resource/common/custom/ko-route/main.js'});
    ko.components.register('drop-down', {require: '/resource/common/custom/dropdown/main.js'});
    ko.components.register('custom-menu', {require: '/resource/common/custom/menu/main.js'});
    ko.components.register('custom-card', {require: '/resource/common/custom/card/main.js'});
    ko.components.register('custom-tree', {require: '/resource/common/custom/tree/main.js'});

    //特效组件
    ko.components.register('canvas-three', {require: '/resource/visitor/canvas/three/main.js'});
    ko.components.register('bongo-cat', {require: '/resource/visitor/plugin/bongo-cat/main.js'});

    //系统管理组件
})(this, ko);
