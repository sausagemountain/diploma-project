package com.interop.java.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

class Client{
    static HashMap<String, Object> Call(String className, Object object, String method, Object[] arguments, String endpoint){
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("object", object);
            data.put("arguments", arguments);

            URL url = new URL(new URL(new URL(endpoint), "./api/"), className + "/" + method);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            connection.setRequestMethod("POST");
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    static HashMap<String, Object> New(String className, Object[] arguments, String endpoint){
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("arguments", arguments);

            URL url = new URL(new URL(new URL(endpoint), "./api/"), className);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            connection.setRequestMethod("POST");
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}