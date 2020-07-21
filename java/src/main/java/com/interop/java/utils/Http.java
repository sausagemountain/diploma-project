package com.interop.java.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Http {
    public static Gson gson = new Gson();

    public static String readData(URLConnection con) {
        String result = "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            result = response.toString();
        } catch (IOException ignored) {
        }
        return result;
    }

    public static HttpURLConnection connectionFor(URL url) throws IOException {
        return (HttpURLConnection) (url.openConnection());
    }

    public static HttpURLConnection postJson(HttpURLConnection con, Object data) {
        try {
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            String input = Http.gson.toJson(data);
            try (OutputStream os = con.getOutputStream()) {
                byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
                os.write(bytes, 0, bytes.length);
            }
            return con;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
