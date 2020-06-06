package com.interop.java.controllers;

import com.interop.java.Registrator;
import com.interop.java.utils.Generation;
import com.interop.java.utils.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api")
@ResponseBody
public class ApiController {
    private static final ScheduledExecutorService cleaner = new ScheduledThreadPoolExecutor(2);
    private final HashMap<String, Object> allObjects = new HashMap<>();

    {
        allObjects.put(String.join(" ", Registrator.class.getName(), "0"), Registrator.getInstance());
        cleaner.scheduleAtFixedRate(allObjects::clear, 60 * 60, 60 * 60, TimeUnit.SECONDS);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{className}")
    public Object newObject(@PathVariable String className, @RequestBody Map<String, Object> input) {
        HashMap<String, Object> response = new HashMap<>();
        if (input == null) {
            return null;
        }
        try {
            String id = (String) input.get("id");
            if (id != null) {
                response.put("id", id);
                response.put("result", allObjects.get(String.join(" ", className, id)));
            } else {
                do {
                    id = Generation.id(10);
                } while (allObjects.containsKey(String.join(" ", className, id)));

                Object obj = null;
                Object[] arguments = (Object[]) input.get("arguments");
                Class clazz = Registrator.getInstance().getLocalClasses(className).keySet().stream().findFirst().get();
                if (arguments != null) {
                    obj = clazz.getConstructor((Class<?>[]) Arrays.stream(arguments).map(Object::getClass).toArray())
                            .newInstance(arguments);
                } else {
                    obj = clazz.newInstance();
                }
                allObjects.put(String.join(" ", className, id), obj);
                response.put("result", Http.gson.toJson(obj));
                response.put("id", id);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            response.put("error", "cannot complete operation");
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{className}/{methodName}")
    public Object callMethod(@PathVariable String className,
                             @PathVariable String methodName,
                             @RequestBody Map<String, Object> input) {
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
                } else {
                    object = allObjects.get(String.join(" ", className, id));
                }
            } else {
                if (object != null) {
                    do {
                        id = Generation.id(10);
                    } while (allObjects.containsKey(String.join(" ", className, id)));
                    allObjects.put(String.join(" ", className, id), object);
                } else {
                    throw new Exception();
                }
            }
            ArrayList<Object> arguments = (ArrayList<Object>) input.get("arguments");
            Class classType = Registrator.getInstance().getLocalClasses(className)
                    .entrySet().stream().findFirst().get().getKey();
            if (!Registrator.getInstance().getLocalClasses(className, methodName).isEmpty() &&
                    classType.isInstance(object)) {
                response.put("result", Registrator.getInstance().getLocalClasses(className, methodName).entrySet()
                        .stream().findFirst().get().getValue().get(0).invoke(object, arguments.toArray()));
                response.put("object", Http.gson.toJson(object));
            }
        } catch (Throwable e) {
            e.printStackTrace();
            response.put("error", "cannot complete operation");
        }
        return response;
    }


}
