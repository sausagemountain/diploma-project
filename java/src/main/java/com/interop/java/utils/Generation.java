package com.interop.java.utils;

import java.util.Base64;
import java.util.Random;

public class Generation {
    static Random rand = new Random();

    public static String id(Integer length) {
        byte[] array = new byte[length];
        rand.nextBytes(array);
        String result = new String(Base64.getEncoder().encode(array));
        return result;
    }
}
