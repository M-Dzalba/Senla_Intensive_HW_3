package ru.dzalba.utils;

import ru.dzalba.annotations.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ComponentScanner {

    public static Set<Class<?>> scanForComponents(String basePackage) throws ClassNotFoundException, IOException {
        Set<Class<?>> components = new HashSet<>();
        String packagePath = basePackage.replace('.', '/');
        File directory = new File(Thread.currentThread().getContextClassLoader().getResource(packagePath).getFile());

        if (directory.exists()) {
            scanDirectory(directory, basePackage, components);
        }

        return components;
    }

    private static void scanDirectory(File directory, String packageName, Set<Class<?>> components) throws ClassNotFoundException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                String subPackage = packageName + "." + file.getName();
                scanDirectory(file, subPackage, components); // Recursive call for subdirectories
            } else if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Component.class)) {
                    components.add(clazz);
                }
            }
        }
    }
}
