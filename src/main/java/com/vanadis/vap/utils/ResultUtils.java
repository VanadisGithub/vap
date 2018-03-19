package com.vanadis.vap.utils;

import com.vanadis.vap.model.Result;

public class ResultUtils {

    //当正确时返回的值
    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("OK");
        result.setData(data);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    //当错误时返回的值
    public static Result error(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }


}
