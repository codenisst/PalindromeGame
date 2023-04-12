package ru.codenisst.service;

import ru.codenisst.listeners.Listener;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Modifier;


public class PackageScanner {

    // данный метод позволяет достаточно удобно расширять функционал проекта. Можно реализовать
    // дополнительный "слушатель" под иную игру. Для этого нужно описать соответствующий listener
    // в пакете ru/codenisst/listeners , с соответствующей логикой.
    public Map<String, Listener> getMapListenersInPackageListeners() throws Exception {
        String path = "ru/codenisst/listeners";
        String packageName = "ru.codenisst.listeners";

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL packageURL = classLoader.getResource(path);
        assert packageURL != null;
        File packageDir = new File(packageURL.getPath());
        File[] classFiles = packageDir.listFiles((dir, name) -> name.endsWith(".class"));

        List<Class<?>> classes = new ArrayList<>();
        Map<String, Listener> objects = new HashMap<>();

        assert classFiles != null;
        for (File file : classFiles) {
            String filePath = file.getPath();
            if (filePath.endsWith(".class")) {
                String className = packageName + '.' + filePath
                        .substring(filePath.lastIndexOf("\\") + 1)
                        .replace(".class", "");
                Class<?> clazz = Class.forName(className);
                if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
                    classes.add(clazz);
                }
            }
        }

        for (Class<?> clazz : classes) {
            Object object = clazz.getDeclaredConstructor().newInstance();
            String key = object.getClass().getName()
                    .substring(object.getClass().getName().lastIndexOf(".") + 1);
            objects.put(key, (Listener) object);
        }

        return objects;
    }
}
