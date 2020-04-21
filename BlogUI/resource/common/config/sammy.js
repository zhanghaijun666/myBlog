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
                this.redirect('#user');
                return;
            }
            root.setRootTemplate('login-page');
        });
        this.get(/\#visitor(.*)/, function () {
            root.setRootTemplate('route-visitor');
        });
        this.get(/\#user(.*)/, function () {
            if (!root.loginUser()) {
                this.redirect('#login');
                return;
            }
            root.setRootTemplate('route-user');
        });
        this.get(/\#admin(.*)/, function () {
            if (!root.loginUser()) {
                this.redirect('#login');
                return;
            }
            root.setRootTemplate('route-admin');
        });
        this.get(/.+/, function () {
            this.redirect("#login");
        });
        this.around(function (callback) {
            if (!location.hash) {
                this.redirect("#login");
                return;
            }
            root.routeUrl(customDecodeURI(window.location.hash));
            root.getLoginUser(function () {
                callback();
            });
        });
    }).run();
    return sammy;
}

window.SammyPage = SammyPage;
