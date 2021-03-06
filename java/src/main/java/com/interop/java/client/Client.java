package com.interop.java.client;

import com.interop.java.utils.Http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Client {
    public static HashMap<String, Object> call(String className, Object object, String id,
                                               String method, Object[] arguments, String endpoint) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("object", object);
            data.put("id", id);
            data.put("arguments", arguments);

            URL url = new URL(new URL(new URL(endpoint), "/api/"), className + "/" + method);
            HttpURLConnection connection = Http.connectionFor(url);
            Http.postJson(connection, data);

            return Http.gson.fromJson(Http.readData(connection), data.getClass());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> _new(String className, Object[] arguments, String endpoint) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("arguments", arguments);

            URL url = new URL(new URL(new URL(endpoint), "/api/"), className);
            HttpURLConnection connection = Http.connectionFor(url);
            Http.postJson(connection, data);

            return Http.gson.fromJson(Http.readData(connection), data.getClass());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}