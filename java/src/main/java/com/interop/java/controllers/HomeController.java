package com.interop.java.controllers;

import com.interop.java.Generation;
import com.interop.java.Registrator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller("/")
@ResponseBody
public class HomeController {
    private HashMap<String, Object> allObjects = new HashMap<>();

    {
        allObjects.put(String.join(" ", Registrator.class.getName(), "0"), Registrator.getInstance());
    }
    
    @RequestMapping(method = RequestMethod.POST, path = "/{className}")
    public Object newObject(@PathVariable String className, Map<String, Object> input) {

        HashMap<String, Object> response = new HashMap<>();

        if (input == null) {
            return null;
        }

        try {
            String id = (String) input.get("id");
            if (id != null) {
                response.put("id", id);
                response.put("result", allObjects.get(String.join(" ",className, id)));
            }
            else {
                do {
                    id = Generation.id(10);
                } while (allObjects.containsKey(String.join(" ",className, id)));
                response.put("id", id);
                Object obj = null;
                Object[] arguments = (Object[]) input.get("arguments");
                if (arguments != null) {
                    obj = Class.forName(className).getConstructor((Class<?>[]) Arrays.stream(arguments).map(e -> e.getClass()).toArray()).newInstance(arguments);
                }
                else {
                    obj = Class.forName(className).newInstance();
                }

                allObjects.put(String.join(" ", className, id), obj);
                response.put("result", obj);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            response.put("error", "cannot complete operation");
        }
        return response;
    }
    @RequestMapping(method = RequestMethod.POST, path = "/{className}/{methodName}")
    public Object callMethod(@PathVariable String className, @PathVariable String methodName, Map<String, Object> input) {
        HashMap<String, Object> response = new HashMap<>();

        if (input == null) {
            return null;
        }

        try {
            Object object = input.get("object");
            String id = (String) input.get("id");
            if (id != null) {
                if (input.containsKey("object")) {
                    allObjects.put(String.join(" ", className, id), object);
                }
                    else {
                    object = allObjects.get(String.join(" ", className, id));
                }
            }
            else {
                if (object != null) {
                    do {
                        id = Generation.id(10);
                    } while (allObjects.containsKey(String.join(" ", className, id)));
                    allObjects.put(String.join(" ", className, id), object);
                }
                else {
                    throw new Exception();
                }
            }

            Object[] arguments = (Object[]) input.get("arguments");
            Class classType = Class.forName(className);
            if (Registrator.getInstance().getLocalClasses().containsKey(className) && Registrator.getInstance().getLocalClasses().get(className).contains(methodName) && classType.isInstance(object)) {
                response.put("result", Registrator.getInstance().getLocalMethods().stream().filter((Method m) -> m.getName().equals(methodName) && m.getDeclaringClass().getName().equals(className)).findFirst().get());
                response.put("object", object);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            response.put("error", "cannot complete operation");
        }
        return response;
    }


}
