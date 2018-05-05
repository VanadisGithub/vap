package com.vanadis.vap.utils;

import com.vanadis.vap.Api.TenXunAIApi;
import com.vanadis.vap.test.Main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class SignUtils {

    //腾讯AI签名
    public static String sign(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)) {
                try {
                    sb.append(k + "=" + URLEncoder.encode(String.valueOf(v), "utf-8") + "&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        sb.append("app_key=" + TenXunAIApi.AppKey);
        String sign = sb.toString();
        sign = MD5utils.MD5Encode(sign, "utf-8");
        sign = sign.toUpperCase();//转大写
        return sign;
    }
}
