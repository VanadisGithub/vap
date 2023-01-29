package com.vanadis.vap.Api;

import com.alibaba.fastjson.JSONObject;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.RegexUtils;
import com.vanadis.vap.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TenXunAIApi {

    public static int AppID = 1106856622;
    public static String AppKey = "yn8xOKpmDX777D7K";
    public static String textchat = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
    public static String texttrans = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttrans";
    public static String wordseg = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_wordseg";
    public static String generalocr = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_generalocr";

    public static JSONObject wordsegApi(String text) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("text", text);
        String result = doApi(wordseg, parameters);
        JSONObject resultObj = JSONObject.parseObject(result).getJSONObject("data");
        return resultObj;

    }

    public static String texttransApi(String text, int type) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("text", text);
        parameters.put("type", type);
        String result = doApi(texttrans, parameters);
        String resultStr = JSONObject.parseObject(result).getJSONObject("data").getString("trans_text");
        return resultStr;

    }

    public static void main(String[] args) {
        String o = textchatApi("10000", "今天天气");
        System.out.println(o);
    }

    public static String textchatApi(String session, String question) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("session", session);
        parameters.put("question", question);
        String result = doApi(textchat, parameters);
        String resultStr = JSONObject.parseObject(result).getJSONObject("data").getString("answer");
        return resultStr;
    }

    /**
     * 图片识别
     *
     * @param session
     * @param question
     * @return
     */
    public static String generalocr(String session, String question) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("session", session);
        parameters.put("question", question);
        return doApi(textchat, parameters);
    }

    public static String doApi(String url, SortedMap<Object, Object> parameters) {
        parameters.put("app_id", AppID);
        parameters.put("time_stamp", System.currentTimeMillis() / 1000);
        parameters.put("nonce_str", RegexUtils.getRandomString(17));
        parameters.put("sign", SignUtils.sign(parameters));
        StringBuffer dataStrSb = new StringBuffer();
        for (Map.Entry<Object, Object> entry : parameters.entrySet()) {
            try {
                dataStrSb.append(entry.getKey() + "=" + URLEncoder.encode(String.valueOf(entry.getValue()), "utf-8") + "&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        dataStrSb = dataStrSb.delete(dataStrSb.length() - 1, dataStrSb.length());
        String result = HttpUtils.doGet(url + "?" + dataStrSb.toString(), null, null);
        return result;
    }

}
