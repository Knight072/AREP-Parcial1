package edu.eci.arep;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PathServices {
    private static PathServices instance;
    private Map<String, Method> services;

    public static PathServices getInstance() {
        if (instance == null) {
            instance = new PathServices();
        }
        return instance;
    }

    public PathServices() {
        try {
            Class c = Class.forName(RestServices.class.getName());
            services = new HashMap<>();
            //Cargar componentes
            if (c.isAnnotationPresent(RestController.class)) {
                Method[] methods = c.getDeclaredMethods();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(GetMapping.class)) {
                        String key = m.getAnnotation(GetMapping.class).value();
                        services.put(key, m);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Method findMappingMethod(String path, String method) {
        if ("GET".equalsIgnoreCase(method)) {
            return getInstance().getServices().get(path);
        } else {
            // Handle case where there is no handler for the method
            return null;
        }
    }

    public Map<String, Method> getServices() {
        return services;
    }
}
