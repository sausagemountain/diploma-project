package com.interop.java;

import com.interop.java.utils.Generation;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Registrator {
    private static final Registrator instance;

    static {
        instance = new Registrator();
        try {
            instance.addMethod(Registrator.class.getMethod("AddModule", String.class, String.class));
            instance.addMethod(Registrator.class.getMethod("AddModule", String.class, String.class, String.class));
            instance.addMethod(Registrator.class.getMethod("RemoveModule", String.class));
            instance.addMethod(Generation.class.getMethod("id", Integer.class));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private final LinkedList<Method> localMethods = new LinkedList<Method>();
    private final HashMap<String, URL> modules = new HashMap<>();
    private final HashMap<String, URL> guiModules = new HashMap<>();

    public static Registrator getInstance() {
        return instance;
    }

    public Map<String, List<String>> getLocalClassNames() {
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

    public Map<Class, List<Method>> getLocalClasses() {
        return getLocalMethods().stream()
                .map(m -> m.getDeclaringClass())
                .distinct()
                .collect(Collectors.toMap(
                        k -> k,
                        v -> getLocalMethods().stream()
                                .filter(m -> m.getDeclaringClass().equals(v))
                                .collect(Collectors.toList())));
    }

    public Map<Class, List<Method>> getLocalClasses(String className) {
        return getLocalMethods().stream()
                .map(Method::getDeclaringClass)
                .filter(c -> c.getName().equals(className) || Arrays.stream(c.getName().split("\\.")).reduce((first, second) -> second).get().equals(className))
                .distinct()
                .collect(Collectors.toMap(
                        k -> k,
                        v -> getLocalMethods().stream()
                                .filter(m -> m.getDeclaringClass().equals(v))
                                .collect(Collectors.toList())));
    }

    public Map<Class, List<Method>> getLocalClasses(String className, String methodName) {
        return getLocalClasses(className)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k.getKey(),
                        v -> getLocalMethods().stream()
                                .filter(m -> m.getDeclaringClass().equals(v.getKey()))
                                .filter(m -> m.getName().equals(methodName))
                                .collect(Collectors.toList())));
    }

    public List<Method> getLocalMethods() {
        return localMethods;
    }

    public void addMethod(Method method) {
        localMethods.add(method);
    }

    public void removeMethod(Method method) {
        localMethods.remove(method);
    }

    public Map<String, URL> getModules() {
        return modules;
    }

    public Map<String, URL> getGuiModules() {
        return guiModules;
    }

    public void AddModule(String name, String uri) {
        try {
            modules.put(name, new URL(uri));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void AddModule(String name, String uri, String guiUri) {
        try {
            modules.put(name, new URL(uri));
            guiModules.put(name, new URL(guiUri));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void RemoveModule(String name) {
        modules.remove(name);
        guiModules.remove(name);
    }

    public Map<String, String> GuiList() {
        return getGuiModules().entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().toString()));
    }
}
