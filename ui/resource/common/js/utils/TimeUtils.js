(function (exports) {
    //dateFormat(new Date(),"YYYY-mm-dd HH:MM:SS")>>> 2020-01-22 16:45:07
    exports.dateFormat = function (date, fmt) {
        date = isNaN(date) ? (date || new Date()) : new Date(date);
        fmt = fmt || "YYYY-mm-dd";
        let ret;
        const opt = {
            "Y+": date.getFullYear().toString(),        // 年
            "m+": (date.getMonth() + 1).toString(),     // 月
            "d+": date.getDate().toString(),            // 日
            "H+": date.getHours().toString(),           // 时
            "M+": date.getMinutes().toString(),         // 分
            "S+": date.getSeconds().toString()          // 秒
        };
        for (let k in opt) {
            ret = new RegExp("(" + k + ")").exec(fmt);
            if (ret) {
                fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
            }
        }
        return fmt;
    }


})(this);