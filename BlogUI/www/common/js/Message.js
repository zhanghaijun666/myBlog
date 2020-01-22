(function (exports) {
    exports.createAutoStructure = function (storeName, data, isObservable) {
        if (!BlogStore[storeName]) {
            throw storeName + " is not ProtocBuf data";
        }
        const map = {};
        data = data ? BlogStore[storeName].decode(data) : {};
        const keys = Object.keys(BlogStore[storeName].fields);
        $.each(keys, function (key, val) {
            if (/.*id.*/i.test(val)) {
                map[val] = data[val];
                return;
            }
            if (BlogStore[storeName].fields[val].rule === "repeated") {
                map[val] = isObservable ? ko.observableArray(data[val]) : data[val];
                return;
            }
            switch (BlogStore[storeName].fields[val].type) {
                case "sint32":
                case "sint64":
                    map[val] = isObservable ? ko.observable(Number(data[val] || 0)) : Number(data[val] || 0);
                    break;
                case "bool":
                    map[val] = isObservable ? ko.observable(!!data[val]) : !!data[val];
                    break;
                case "string":
                default:
                    map[val] = isObservable ? ko.observable(data[val]) : data[val];
            }
        });
        return map;
    };

    function Message(storeName, data, isObservable) {
        this.storeName = storeName;
        $.extend(this, createAutoStructure(storeName, data, isObservable));
    }

    Message.prototype.getFields = function () {
        return Object.keys(BlogStore[this.storeName].fields);
    };
    Message.prototype.toArrayBuffer = function () {
        const store = ko.deepObservableClone(this);
        const errors = BlogStore[this.storeName].verify(store);
        if (errors) {
            throw new Error(errors);
        }
        return BlogStore[this.storeName].encode(store).finish();
    };


    exports.Message = Message;
})(this);