(function(exports){
    exports.isFunction = function (dataToCheck){
        return jQuery.isFunction(dataToCheck);
    };
    exports.showTimeDateFormat = function(date,noShowTodayTime){
        if (!date) {
            return l10n("file.unknownDate");
        }
        var noTodayTime = noShowTodayTime || false ;

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

        var getHours = fileDate.getHours()>9 ? fileDate.getHours() : "0"+ fileDate.getHours();
        var getMinutes = fileDate.getMinutes()>9 ? fileDate.getMinutes() : "0"+ fileDate.getMinutes();
        if (fileTime >= todayBegin) {
            return noTodayTime ? l10n("time.today") : getHours + ":"+ getMinutes;
        }
        if (fileTime >= yesterdayBegin) {
            return l10n("time.yesterday");
        }
        if (isInSameWeek) {
            return l10n(L10N_WEEKS[fileDayOfWeek]);
        }
        if (isInSameYear) {
            return l10nIf("time.dayOfMonth", fileDate.simpleFormat("MM/dd/yyyy"), {month: fileDate.getMonth() + 1, day: fileDate.getDate()});
        }
        return l10nIf("time.dayOfYear", fileDate.simpleFormat("MM/dd/yyyy"), {year: fileDate.getFullYear(), month: fileDate.getMonth() + 1, day: fileDate.getDate()});
    }
})(this);