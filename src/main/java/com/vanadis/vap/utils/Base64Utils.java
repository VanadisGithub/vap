package com.vanadis.vap.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Utils {

    public static String encode(String str) {
        String encodedText = null;
        try {
            byte[] textByte = str.getBytes("UTF-8");
            Base64.Encoder encoder = Base64.getEncoder();
            encodedText = encoder.encodeToString(textByte);
        } catch (UnsupportedEncodingException e) {
            System.out.println("编码失败！");
            e.printStackTrace();

        }
        return encodedText;
    }

    public static String decode(String str) {

        String decoderText = null;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            decoderText = new String(decoder.decode(str), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("编码失败！");
            e.printStackTrace();

        }
        return decoderText;
    }

}
