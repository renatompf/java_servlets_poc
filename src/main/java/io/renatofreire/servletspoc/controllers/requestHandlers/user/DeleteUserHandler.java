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
import java.util.Optional;


public class DeleteUserHandler implements RequestHandler {

    private final UserService userService;

    public DeleteUserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Integer id = PathUtils.extractId(request.getPathInfo()).orElseThrow(() -> new NumberFormatException("Invalid ID format"));
            userService.deleteUser(id);
        } catch (NumberFormatException e) {
            ResponseSenderUtils.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        } catch (Exception e) {
            ResponseSenderUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}