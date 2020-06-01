package com.interop.java.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Http {
    public static Gson gson = new Gson();

    public static String readData(URLConnection con){
        String result = "";
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            result = response.toString();
        } catch (IOException ignored){ }
        return result;
    }
    public static void writeData(URLConnection con, String input){
        try(OutputStream os = con.getOutputStream()) {
            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            os.write(bytes, 0, bytes.length);
        }
        catch (IOException ignored){ }
    }
    public static HttpURLConnection connectionFor(URL url) throws IOException {
        return (HttpURLConnection) (url.openConnection());
    }
}
