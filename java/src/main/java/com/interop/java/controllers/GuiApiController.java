package com.interop.java.controllers;

import com.interop.java.Registrator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.stream.Collectors;

@Controller("/gui")
@ResponseBody
public class GuiApiController {
    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public Map<String, String> getGuis(){
        return Registrator.getInstance().getGuiModules().entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toString()));
    }
}
