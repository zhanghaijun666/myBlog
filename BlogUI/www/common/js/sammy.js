/* global FileUrl */

function SammyPage(options) {
    options = options || {};
    var root = options.view;
    var sammy = Sammy(function () {
        this.get(/\#login(.*)/, function () {
            root.setRootTemplate('login-page');
        });
        this.get(/\#message(.*)/, function () {
            root.setRootTemplate('message');
        });
        this.get(/\#admin(.*)/, function () {
            toastShowMsg("暂未开发，尽情期待！！！");
            this.redirect("#login");
        });
        this.get(/.+/, function () {
            this.redirect("#file");
        });
        this.around(function (callback) {
            callback();
        });
    }).run();
    return sammy;
}
window.SammyPage = SammyPage;
