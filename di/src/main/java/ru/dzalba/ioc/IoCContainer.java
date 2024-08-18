package ru.dzalba.ioc;

import ru.dzalba.utils.ComponentScanner;
import ru.dzalba.utils.ConfigLoader;
import ru.dzalba.annotations.Autowire;
import ru.dzalba.annotations.Component;
import ru.dzalba.annotations.Value;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Method;

public class IoCContainer {

    private static IoCContainer instance;

    private final Map<Class<?>, Class<?>> typeMap = new HashMap<>();
    private final Map<Class<?>, Object> singletonMap = new HashMap<>();

    static {
        try {
            instance = new IoCContainer();
            Set<Class<?>> components = ComponentScanner.scanForComponents("ru.dzalba");
            instance.registerComponents(components);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize IoC Container", e);
        }
    }

    private IoCContainer() {}

    public static synchronized IoCContainer getInstance() {
        if (instance == null) {
            instance = new IoCContainer();
        }
        return instance;
    }

    public void registerComponents(Set<Class<?>> componentClasses) {
        for (Class<?> clazz : componentClasses) {
            if (clazz.isAnnotationPresent(Component.class)) {
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces.length > 0) {
                    for (Class<?> iface : interfaces) {
                        register(iface, clazz);
                    }
                } else {
                    register(clazz, clazz);
                }
            }
        }
    }

    private void register(Class<?> type, Class<?> implementation) {
        System.out.println("Registered: " + type.getName() + " -> " + implementation.getName());
        typeMap.put(type, implementation);
    }

    public <T> T resolve(Class<T> type) throws Exception {
        if (singletonMap.containsKey(type)) {
            return type.cast(singletonMap.get(type));
        }

        Class<?> implementationType = typeMap.get(type);
        if (implementationType == null) {
            throw new RuntimeException("No implementation registered for " + type.getName());
        }

        Constructor<?> constructor = findSuitableConstructor(implementationType.getConstructors());
        assert constructor != null;
        Object[] constructorArgs = resolveConstructorArguments(constructor);

        Object instance = constructor.newInstance(constructorArgs);

        injectFields(instance);
        injectMethods(instance);

        singletonMap.put(type, instance);
        return type.cast(instance);
    }

    private Constructor<?> findSuitableConstructor(Constructor<?>[] constructors) {
        return constructors.length > 0 ? constructors[0] : null;
    }

    private Object[] resolveConstructorArguments(Constructor<?> constructor) throws Exception {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = resolve(parameterTypes[i]);
        }
        return args;
    }

    private void injectFields(Object instance) throws Exception {
        Class<?> clazz = instance.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowire.class)) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object dependency = resolve(fieldType);
                field.set(instance, dependency);
            } else if (field.isAnnotationPresent(Value.class)) {
                Value valueAnnotation = field.getAnnotation(Value.class);
                String propertyValue = ConfigLoader.getProperty(valueAnnotation.value());
                field.setAccessible(true);
                field.set(instance, propertyValue);
            }
        }
    }

    private void injectMethods(Object instance) throws Exception {
        Class<?> clazz = instance.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowire.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    Object dependency = resolve(parameterTypes[0]);
                    method.invoke(instance, dependency);
                }
            }
        }
    }
}