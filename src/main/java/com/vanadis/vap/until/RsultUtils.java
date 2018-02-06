package com.vanadis.vap.until;

public class RsultUtils {

    //当正确时返回的值
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

    public static class Result<T> {

        private int code;//状态码
        private String msg;//信息
        private Object data;//数据

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
