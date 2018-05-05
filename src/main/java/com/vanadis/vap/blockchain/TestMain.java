package com.vanadis.vap.blockchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class TestMain {

    public static void main(String[] args) {
        String vanadis = "vanadis";

        String str = StringUtil.applySha256(vanadis + System.currentTimeMillis());
        System.out.println(str.length());
    }

}
