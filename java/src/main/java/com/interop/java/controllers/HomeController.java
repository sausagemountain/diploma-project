package com.interop.java.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Controller("/")
@ResponseBody
public class HomeController {
    @RequestMapping(method = RequestMethod.POST, path = "/{className}")
    public Object post(@PathVariable String className, Map<String, Object> input) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Class<?> clazz = Class.forName(String.valueOf(className));
            Object object;
            if (input.containsKey("arguments")){
                object = clazz.getConstructor().newInstance(((Object[]) input.get("arguments")));
            }
            else {
                object = clazz.newInstance();
            }
            map.put("result", object);
        } catch (Throwable e) {
            e.printStackTrace();
            map.put("error", "cannot complete operation");
        }

        return map;
    }
    @RequestMapping(method = RequestMethod.POST, path = "/{className}/{methodName}")
    public Object post(@PathVariable String className, @PathVariable String methodName, Map<String, Object> input) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Class<?> clazz = Class.forName(String.valueOf(className));
            Object object = clazz.cast(input.get("object"));
            Object[] arguments = (Object[]) input.get("arguments");
            Method method = clazz.getMethod(String.valueOf(methodName));
            method.setAccessible(true);
            map.put("result", method.invoke(object, arguments));
        } catch (Throwable e) {
            e.printStackTrace();
            map.put("error", "cannot complete operation");
        }

        return map;
    }


}
