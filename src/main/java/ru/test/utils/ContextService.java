package ru.test.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContextService {

    private static ContextService instance;

    private Map<Class<?>, Object> serviceMap = new ConcurrentHashMap<>();

    private ContextService() {
    }

    public static ContextService getInstance() {
        if (null == instance) {
            instance = new ContextService();
        }
        return instance;
    }

    public void addService(Object service) {
        serviceMap.put(service.getClass().getSuperclass(), service);
    }

    public <T> T getService(Class<T> aServiceType) {
        return (T) serviceMap.get(aServiceType);
    }

    public void removeService(Class<?> aServiceType) {
        serviceMap.remove(aServiceType);
    }
}
