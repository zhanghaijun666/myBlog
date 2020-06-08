(function (exports) {
    function getObjAttr(obj, attr) {
        if (!obj || !attr) {
            return undefined;
        }
        var array = attr.split(".");
        var objAttr = obj;
        for (var i = 0; i < array.length; i++) {
            objAttr = objAttr[array[i]];
            if (!objAttr) {
                return undefined;
            }
        }
        return objAttr;
    }

    exports.createAutoStructure = function (storeName, data, isObservable) {
        let BlogStoreAttr = getObjAttr(BlogStore, storeName);
        if (!BlogStoreAttr) {
            throw storeName + " is not ProtocBuf data";
        }
        const map = {};
        if (data instanceof Uint8Array) {
            data = BlogStoreAttr.decode(data)
        }
        data = data ? data : {};
        const keys = Object.keys(BlogStoreAttr.fields);
        $.each(keys, function (key, val) {
            if (/.*id.*/i.test(val)) {
                map[val] = data[val];
                return;
            }
            if (BlogStoreAttr.fields[val].rule === "repeated") {
                map[val] = isObservable ? ko.observableArray(data[val]) : data[val];
                return;
            }
            switch (BlogStoreAttr.fields[val].type) {
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