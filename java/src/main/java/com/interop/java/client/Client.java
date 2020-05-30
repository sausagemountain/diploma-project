package com.interop.java.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

class Client{
    static HashMap<String, Object> Call(Object object, String method, Object[] arguments, String endpoint){
        try {
            String className = object.getClass().getName();
            HashMap<String, Object> data = new HashMap<>();
            data.put("object", object);
            data.put("arguments", arguments);

            URL url = new URL(new URL(endpoint), className + "/" + method);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
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