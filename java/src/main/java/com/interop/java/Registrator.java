package com.interop.java;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Registrator {
    public static Registrator getInstance(){
        return instance;
    }
    private static final Registrator instance;
    static {
        instance = new Registrator();
        try {
            instance.AddMethod(Registrator.class.getMethod("AddModule", String.class, String.class));
            instance.AddMethod(Registrator.class.getMethod("AddModule", String.class, String.class, String.class));
            instance.AddMethod(Registrator.class.getMethod("RemoveMethod", Method.class));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public Map<String, List<String>> getLocalClasses() {
        return getLocalMethods().stream()
                .map(m -> m.getDeclaringClass().getName())
                .distinct()
                .collect(Collectors.toMap(
                        k -> k,
                        v -> getLocalMethods().stream()
                                .filter(m -> m.getDeclaringClass().getName().equals(v))
                                .map(Method::getName)
                                .collect(Collectors.toList())));
    }

    private final LinkedList<Method> localMethods = new LinkedList<Method>();

    public List<Method> getLocalMethods() {
        return localMethods;
    }

    public void AddMethod(Method method)
    {
        localMethods.add(method);
    }

    public void RemoveMethod(Method method)
    {
        localMethods.remove(method);
    }


    private final HashMap<String, URL> modules = new HashMap<>();
    public Map<String, URL> getModules() {
        return modules;
    }

    private final HashMap<String, URL> guiModules = new HashMap<>();
    public Map<String, URL> getGuiModules() {
        return guiModules;
    }

    public void AddModule(String name ,String uri)
    {
        try {
            modules.put(name, new URL(uri));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void AddModule(String name, String uri, String guiUri)
    {
        try {
            modules.put(name, new URL(uri));
            guiModules.put(name, new URL(guiUri));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void RemoveModule(String name)
    {
        modules.remove(name);
        guiModules.remove(name);
    }

    public Map<String, String> GuiList()
    {
        return getGuiModules().entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().toString()));
    }
}
