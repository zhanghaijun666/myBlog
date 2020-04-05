/* global FileUrl */

function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get(/\#demo(.*)/, function () {
            root.setRootTemplate('bongo-cat');
        });
        this.get(/\#login(.*)/, function () {
            if (root.loginUser()) {
                this.redirect('#admin');
                return;
            }
            root.setRootTemplate('login-page');
        });
        this.get(/\#visitor(.*)/, function () {
            root.setRootTemplate('layout-visitor');
        });
        this.get(/\#user(.*)/, function () {
            if (!root.loginUser()) {
                this.redirect('#login');
                return;
            }
            root.setRootTemplate('layout-user');
        });
        this.get(/\#admin(.*)/, function () {
            if (!root.loginUser()) {
                this.redirect('#login');
                return;
            }
            root.setRootTemplate('layout-admin');
        });
        this.get(/.+/, function () {
            this.redirect("#login");
        });
        this.around(function (callback) {
            if (!location.hash) {
                this.redirect("#login");
                return;
            }
            root.routeHash(customDecodeURI(window.location.hash));
            root.getLoginUser(function (user) {
                callback();
            });
        });
    }).run();
    return sammy;
}

window.SammyPage = SammyPage;
