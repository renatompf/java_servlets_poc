package io.renatofreire.servletspoc.core;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {


    private final Map<String, Map<String, RequestHandler>> registry = new HashMap<>();

    public void register(String method, String path, RequestHandler handler) {
        registry
                .computeIfAbsent(method.toUpperCase(), m -> new HashMap<>())
                .put(path, handler);
    }

    public RequestHandler getHandler(String method, String path) {
        Map<String, RequestHandler> methodHandlers = registry.get(method.toUpperCase());
        if (methodHandlers != null) {
            return methodHandlers.get(path);
        }
        return null;
    }

}