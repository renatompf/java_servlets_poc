package io.renatofreire.servletspoc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResponseSenderUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(Map.of("error", message)));
    }

    public static void sendSuccess(HttpServletResponse response, Object data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(data));
    }

    public static void sendCreated(HttpServletResponse response, Object data) throws IOException {
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(data));
    }

    public static <T> void sendValidationErrors(HttpServletResponse response, Set<ConstraintViolation<T>> violations) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        List<Map<String, String>> errors = violations.stream()
                .map(v -> Map.of(
                        "field", v.getPropertyPath().toString(),
                        "message", v.getMessage()
                ))
                .toList();
        mapper.writeValue(response.getWriter(), Map.of("errors", errors));
    }

}
