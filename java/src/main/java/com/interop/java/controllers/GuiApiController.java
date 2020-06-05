package com.interop.java.controllers;

import com.interop.java.Registrator;
import com.interop.java.utils.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gui")
@ResponseBody
public class GuiApiController {
    @RequestMapping(method = RequestMethod.GET, path = "/here")
    public Map<String, String> getGuis(){
        return Registrator.getInstance().getGuiModules().entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toString()));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public Map<String, String> getAllGuis(){
        try {
            HashMap<String, String> all = new HashMap<>(getGuis());
            List<String> lines = Files.readAllLines(Paths.get("gui_uris.conf"));
            lines.addAll(Registrator.getInstance().getModules().keySet());
            for (String line : lines) {
                URL url = new URL(new URL(line), "/gui/here");
                HttpURLConnection connection = Http.connectionFor(url);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                String result = Http.readData(connection);
                HashMap<String, String> map = Http.gson.fromJson(result, all.getClass());
                if (map != null && !map.isEmpty()) {
                    map.forEach(all::put);
                }
            }
            return all;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
