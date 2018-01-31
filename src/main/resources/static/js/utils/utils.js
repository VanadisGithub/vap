var Utils = {

    setCookie: function (c_name, value, expiredays) {
        var exdate = new Date();
        exdate.setDate(exdate.getDate() + expiredays);
        document.cookie = c_name + "=" + encodeURI(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
    },

    getCookie: function (c_name) {
        if (document.cookie.length > 0) {
            c_start = document.cookie.indexOf(c_name + "=");
            if (c_start != -1) {
                c_start = c_start + c_name.length + 1;
                c_end = document.cookie.indexOf(";", c_start);
                if (c_end == -1) c_end = document.cookie.length;
                return decodeURI(document.cookie.substring(c_start, c_end));
            }
        }
        return ""
    },

    //根据逗号和换行分割
    splitUrl: function (url) {
        url = url.replace("，", ",");
        var temp = url.split(/[\n,]/g);
        for (var i = 0; i < temp.length; i++) {
            if (this.isUfOrNullOrEmpty(temp[i])) {
                temp.splice(i, 1);
                i--; //删除数组索引位置应保持不变
            }
        }
        return temp;
    },

    //判断值是否为undefined或者null或者为空
    isUfOrNullOrEmpty: function (val) {
        return val == null || val == 'undefined' || val == "";
    },

    //复制到粘贴板
    copyToClipboard: function (str) {
        var save = function (e) {
            e.clipboardData.setData('text/plain', str);//下面会说到clipboardData对象
            e.preventDefault();//阻止默认行为
        }
        document.addEventListener('copy', save);
        document.execCommand("copy");//使文档处于可编辑状态，否则无效
    },

    isNumber: function (val) {
        var regPos = /^\d+(\.\d+)?$/; //非负浮点数
        var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
        if (regPos.test(val) || regNeg.test(val)) {
            return true;
        } else {
            return false;
        }
    }
}




