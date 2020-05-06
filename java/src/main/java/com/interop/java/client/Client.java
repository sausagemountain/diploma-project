package com.interop.java.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

class Client{
    static HashMap<String, Object> Call(Object object, String method, Object[] arguments, String endpoint){
        try {
            String className = object.getClass().getName();
            HashMap<String, Object> data = new HashMap<>();
            data.put("class", className);
            data.put("object", object);
            data.put("method", method);
            data.put("arguments", arguments);

            HttpURLConnection connection = (HttpURLConnection) (new URL(endpoint).openConnection());
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.connect();
            connection.disconnect();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}