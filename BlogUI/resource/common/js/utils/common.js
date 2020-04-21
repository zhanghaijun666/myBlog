(function (exports) {
    exports.isFunction = function (dataToCheck) {
        return jQuery.isFunction(dataToCheck);
    };
    exports.getStoreArray = function (storeKey) {
        var storage = window.localStorage;
        if (storage) {
            var str = storage.getItem(storeKey);
            if (str) {
                //需要加密
                if (str) {
                    try {
                        var obj = JSON.parse(str);
                        str = null;
                        return obj;
                    } catch (e) {
                    }
                }
            }
        }
        return null;
    };
    exports.storeLocalArray = function (storeKey, obj) {
        var storage = window.localStorage;
        if (storage) {
            if (typeof obj === "string") {
                obj = obj.trim();
            }
            var str = JSON.stringify(obj);
            //需要解密
            storage.setItem(storeKey, str);
        }
    };
    exports.formatLongSize = function (limit) {
        var size = "";
        if (limit < 0.1 * 1024) { //如果小于0.1KB转化成B
            size = limit.toFixed(2) + "B";
        } else if (limit < 0.1 * 1024 * 1024) {//如果小于0.1MB转化成KB
            size = (limit / 1024).toFixed(2) + "KB";
        } else if (limit < 0.1 * 1024 * 1024 * 1024) { //如果小于0.1GB转化成MB
            size = (limit / (1024 * 1024)).toFixed(2) + "MB";
        } else { //其他转化成GB
            size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB";
        }
        var sizestr = size + "";
        var len = sizestr.indexOf("\.");
        var dec = sizestr.substr(len + 1, 2);
        if (dec === "00") {//当小数点后为00时 去掉小数部分
            return sizestr.substring(0, len) + sizestr.substr(len + 3, 2);
        }
        return sizestr;
    };
    exports.timestampformTime = function (timestamp, format) {
        format = format || "yyyy-MM-dd HH:mm";
        var date;
        if (!timestamp) {
            date = new Date();
        } else if (timestamp instanceof Date) {
            date = timestamp;
        } else if (!isNaN(timestamp)) {
            date = new Date(timestamp);
        }
        if (date) {
            return date.simpleFormat(format);
        }
        return "";
    };
    exports.showTimeDateFormat = function (date, noShowTodayTime) {
        if (!date) {
            return l10n("file.unknownDate");
        }
        var noTodayTime = noShowTodayTime || false;

        var DAY_MILLS = 24 * 60 * 60 * 1000;
        var WEEK_MILLS = DAY_MILLS * 7;
        var L10N_WEEKS = ["time.SUN", "time.MON", "time.TUE", "time.WED", "time.THU", "time.FRI", "time.SAT"];

        var fileTime = Number(date);
        var fileDate = new Date(fileTime);

        var todayBegin = new Date(getServerNowTime());
        todayBegin.setHours(0, 0, 0, 0);
        var yesterdayBegin = todayBegin - DAY_MILLS;

        var fileDayOfWeek = fileDate.getDay();
        var todayEndTime = todayBegin.getTime() + DAY_MILLS - 1;

        var isInSameWeek = (todayEndTime - fileTime <= WEEK_MILLS) && fileDayOfWeek < todayBegin.getDay();
        var isInSameYear = fileDate.getFullYear() === todayBegin.getFullYear();

        var getHours = fileDate.getHours() > 9 ? fileDate.getHours() : "0" + fileDate.getHours();
        var getMinutes = fileDate.getMinutes() > 9 ? fileDate.getMinutes() : "0" + fileDate.getMinutes();
        if (fileTime >= todayBegin) {
            return noTodayTime ? l10n("time.today") : getHours + ":" + getMinutes;
        }
        if (fileTime >= yesterdayBegin) {
            return l10n("time.yesterday");
        }
        if (isInSameWeek) {
            return l10n(L10N_WEEKS[fileDayOfWeek]);
        }
        if (isInSameYear) {
            return l10nIf("time.dayOfMonth", fileDate.simpleFormat("MM/dd/yyyy"), {
                month: fileDate.getMonth() + 1,
                day: fileDate.getDate()
            });
        }
        return l10nIf("time.dayOfYear", fileDate.simpleFormat("MM/dd/yyyy"), {
            year: fileDate.getFullYear(),
            month: fileDate.getMonth() + 1,
            day: fileDate.getDate()
        });
    }
})(this);