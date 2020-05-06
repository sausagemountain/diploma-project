package com.interop.java.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Controller("/")
@ResponseBody
public class HomeController {
    @RequestMapping(method = RequestMethod.GET)
    public Object get() {
        return new String[]{"hello", "world"};
    }
    @RequestMapping(method = RequestMethod.POST)
    public Object post(Map<String, Object> input) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            Class<?> clazz = Class.forName(String.valueOf(input.get("class")));
            Object object = clazz.cast(input.get("object"));
            Object[] arguments = (Object[]) input.get("arguments");
            Method method = clazz.getMethod(String.valueOf(input.get("method")));
            method.setAccessible(true);
            map.put("result", method.invoke(object, arguments));
        } catch (Throwable e) {
            e.printStackTrace();
            map.put("error", "cannot complete operation");
        }

        return map;
    }
}
