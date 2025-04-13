package io.renatofreire.servletspoc.controllers.requestHandlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RequestHandler {
    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException;
}