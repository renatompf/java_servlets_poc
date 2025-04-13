package io.renatofreire.servletspoc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.Set;

public class ValidationUtils {

    public static <T> boolean validateAndRespond(T input, Validator validator, ObjectMapper objectMapper, HttpServletResponse response) throws IOException {
        Set<ConstraintViolation<T>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(), violations);
            return false;
        }
        return true;
    }
}