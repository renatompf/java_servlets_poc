package io.renatofreire.servletspoc.controllers.requestHandlers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renatofreire.servletspoc.controllers.requestHandlers.RequestHandler;
import io.renatofreire.servletspoc.dto.UserDtoIn;
import io.renatofreire.servletspoc.model.User;
import io.renatofreire.servletspoc.service.UserService;
import io.renatofreire.servletspoc.utils.PathUtils;
import io.renatofreire.servletspoc.utils.ResponseSenderUtils;
import io.renatofreire.servletspoc.utils.ValidationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.Map;


public class UpdateUserHandler implements RequestHandler {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public UpdateUserHandler(UserService userService, ObjectMapper objectMapper, Validator validator) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {

            Integer id = PathUtils.extractId(request.getPathInfo()).orElseThrow(() -> new NumberFormatException("Invalid ID format"));
            UserDtoIn dtoIn = objectMapper.readValue(request.getReader(), UserDtoIn.class);
            if (!ValidationUtils.validateAndRespond(dtoIn, validator, objectMapper, response)) {
                return;
            }

            User user = new User(id, dtoIn.name(), dtoIn.email(), dtoIn.country());
            userService.updateUser(user);
            ResponseSenderUtils.sendSuccess(response, Map.of("message", "User updated successfully"));
        } catch (NumberFormatException e) {
            ResponseSenderUtils.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        } catch (IllegalArgumentException e) {
            ResponseSenderUtils.sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }catch (Exception e) {
            ResponseSenderUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}