//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {
    //初始界面
    ko.components.register('login-page', {require: '/static/www/visitor/login-page/main.js'});
    ko.components.register('route-visitor', {require: '/static/www/visitor/route/main.js'});
    ko.components.register('route-user', {require: '/static/www/user/route/main.js'});
    ko.components.register('route-admin', {require: '/static/www/admin/route/main.js'});

    //公共组件
    ko.components.register('ko-route', {require: '/static/www/common/custom/ko-route/main.js'});
    ko.components.register('drop-down', {require: '/static/www/common/custom/dropdown/main.js'});
    ko.components.register('custom-menu', {require: '/static/www/common/custom/menu/main.js'});
    ko.components.register('custom-card', {require: '/static/www/common/custom/card/main.js'});
    ko.components.register('custom-tree', {require: '/static/www/common/custom/tree/main.js'});

    //特效组件
    ko.components.register('canvas-three', {require: '/static/www/visitor/canvas/three/main.js'});
    ko.components.register('bongo-cat', {require: '/static/www/visitor/plugin/bongo-cat/main.js'});

    //系统管理组件
})(this, ko);
