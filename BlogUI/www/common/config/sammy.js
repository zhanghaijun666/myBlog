/* global FileUrl */

function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get(/\#demo(.*)/, function () {
            root.setRootTemplate('canvas-three');
        });
        this.get(/\#login(.*)/, function () {
            root.setRootTemplate('login-page');
        });
        this.get(/\#visitor(.*)/, function () {
            root.setRootTemplate('layout-visitor');
        });
        this.get(/\#user(.*)/, function () {
            root.setRootTemplate('layout-user');
        });
        this.get(/\#admin(.*)/, function () {
            root.setRootTemplate('layout-admin');
        });
        this.get(/.+/, function () {
            this.redirect("#login");
        });
        this.around(function (callback) {
            callback();
        });
    }).run();
    return sammy;
}

window.SammyPage = SammyPage;
