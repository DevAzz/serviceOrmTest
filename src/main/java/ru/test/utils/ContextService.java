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
        if (service.getClass().getInterfaces().length != 0) {
            serviceMap.put(service.getClass().getInterfaces()[0], service);
        } else {
            serviceMap.put(service.getClass(), service);
        }
    }

    public <T> T getService(Class<T> aServiceType) {
        return (T) serviceMap.get(aServiceType);
    }

    public void removeService(Class<?> aServiceType) {
        serviceMap.remove(aServiceType);
    }
}
