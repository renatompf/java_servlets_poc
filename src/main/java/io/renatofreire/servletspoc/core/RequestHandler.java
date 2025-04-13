package io.renatofreire.servletspoc.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RequestHandler {
    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException;
}