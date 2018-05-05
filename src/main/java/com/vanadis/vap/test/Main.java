package com.vanadis.vap.test;

import com.vanadis.vap.Api.TenXunAIApi;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.RegexUtils;
import com.vanadis.vap.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {

    public static int AppID = 1106856622;
    public static String AppKey = "yn8xOKpmDX777D7K";
    public static String url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
    public static String fangyiurl = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttrans";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "ipad air/pro 9.7/新ipad 2018 ，，";
        str = "ipad air/pro 9.7/新ipad 2017 ，";

        System.out.println(str.length());

    }
}
