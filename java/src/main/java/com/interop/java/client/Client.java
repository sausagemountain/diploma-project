package com.interop.java.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class Client{
    static HashMap<String, Object> Call(String className, Object object, String id, String method, Object[] arguments, String endpoint){
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("object", object);
            data.put("id", id);
            data.put("arguments", arguments);

            URL url = new URL(new URL(new URL(endpoint), "/api/"), className + "/" + method);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            Gson g = new Gson();
            String jsonInputString = g.toJson(data);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            String result;
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                result = response.toString();
            }
            HashMap<String, Object> map = new HashMap<>();
            map = g.fromJson(result, map.getClass());
            return map;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    static Map<String, Object> New(String className, Object[] arguments, String endpoint){
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("arguments", arguments);

            URL url = new URL(new URL(new URL(endpoint), "/api/"), className);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            Gson g = new Gson();
            String jsonInputString = g.toJson(data);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            String result;
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                result = response.toString();
            }
            HashMap<String, Object> map = new HashMap<>();
            map = g.fromJson(result, map.getClass());
            return map;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}