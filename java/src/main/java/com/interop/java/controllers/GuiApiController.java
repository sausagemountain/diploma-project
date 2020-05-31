package com.interop.java.controllers;

import com.google.gson.Gson;
import com.interop.java.Registrator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("/gui")
@ResponseBody
public class GuiApiController {
    @RequestMapping(method = RequestMethod.GET, path = "/here")
    public Map<String, String> getGuis(){
        return Registrator.getInstance().getGuiModules().entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toString()));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public Map<String, String> getAllGuis(){
        try {
            Map<String, String> all = getGuis();
            List<String> lines = Files.readAllLines(Paths.get("gui_uris.config"));
            for (String line : lines) {
                URL url = new URL(new URL(line), "/gui/here");
                HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
                connection.setRequestMethod("Get");
                connection.setRequestProperty("Accept", "application/json");

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
                HashMap<String, String> map = new HashMap<>();
                Gson g = new Gson();
                map = g.fromJson(result, map.getClass());
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    all.put(entry.getKey(), entry.getValue());
                }
            }
            return all;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
