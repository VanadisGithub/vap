package com.vanadis.vap.test;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static int AppID = 1106856622;
    public static String AppKey = "yn8xOKpmDX777D7K";
    public static String url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
    public static String fangyiurl = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttrans";

    public static void main(String[] args) {

        String s = "小女孩\uD84C\uDD03袍ꪣ";
        try {
            s = new String(s.getBytes("ISO8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

}
