(function (global) {

    global.showDailog = function (options) {
        const container = document.createElement("div");
        container.innerHTML = "<div class=\"modal fade\" aria-hidden=\"true\">\n" +
            "        <div class=\"modal-dialog\" data-bind=\"css: $data.modal\">\n" +
            "            <div class=\"modal-content\">\n" +
            "                <div class=\"modal-header\"\n" +
            "                     data-bind=\"visible:$data.isShowHeader(), template:{name: 'common-template', data: $data.getHeaderParams()}\"></div>\n" +
            "                <div class=\"modal-body\"\n" +
            "                     data-bind=\"template:{name: 'common-template', data: $data.getBodyParams()}\"></div>\n" +
            "                <div class=\"modal-footer\"\n" +
            "                     data-bind=\"visible:$data.isShowFoot(), template:{name: 'common-template', data: $data.getFootParams()}\"></div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>";
        document.body.appendChild(container);
        const dialogModel = new DialogModel(options, container);
        dialogModel.dialogRef.modal({
            backdrop: dialogModel.backdrop,
            keyboard: dialogModel.keyboard
        });
        ko.applyBindings(dialogModel, container);
    };
    global.showInputDailog = function (options) {
        showDailog($.extend({
            header: "提示信息",
            modal: "modal-sm",
            bodyTemplate: "common-dialog-input-body",
            inputValue: ko.observable(""),
            success: function () {
            }
        }, options));
    };

    function DialogModel(options, container) {
        const defaultValue = {
            backdrop: true,// true//false//"static"
            keyboard: true,
            modal: "", //modal-sm //modal-lg//model-full
            header: "",
            headerTemplate: "common-dialog-header",
            bodyTemplate: "common-dialog-body",
            footTemplate: "common-dialog-foot",
            success: null,
            cancel: null
        };
        let self = $.extend(this, defaultValue, options);
        self.dialogRef = $(container).find(".modal");
        self.dialogRef.on('hidden.bs.modal', function () {
            ko.utils.domNodeDisposal.removeNode(container);
            if (isFunction(this.cancel)) {
                this.cancel(this);
            }
        });
    }

    DialogModel.prototype.successEvent = function () {
        this.dialogRef.modal("hide");
        if (isFunction(this.success)) {
            this.success(this, this.dialogRef);
        }
    };
    DialogModel.prototype.isShowHeader = function () {
        return ko.unwrap(this.headerTemplate) !== "common-dialog-header" || ko.unwrap(this.header);
    };
    DialogModel.prototype.isShowFoot = function () {
        return ko.unwrap(this.footTemplate) !== "common-dialog-foot" || isFunction(this.success);
    };
    DialogModel.prototype.getHeaderParams = function () {
        return {name: this.headerTemplate, data: this};
    };
    DialogModel.prototype.getBodyParams = function () {
        return {name: this.bodyTemplate, data: this};
    };
    DialogModel.prototype.getFootParams = function () {
        return {name: this.footTemplate, data: this};
    };
    global.DialogModel = DialogModel;
})(this);