//https://knockoutjs.com/documentation/component-overview.html
(function (global, ko) {
    ko.components.register('login-page', {require: '/static/www/visitor/components/login-page/main.js'});
    ko.components.register('layout-visitor', {require: '/static/www/visitor/components/layout/main.js'});
    ko.components.register('layout-user', {require: '/static/www/user/components/layout/main.js'});
    ko.components.register('layout-admin', {require: '/static/www/admin/components/layout/main.js'});




})(this, ko);
