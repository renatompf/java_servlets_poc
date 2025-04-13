package io.renatofreire.servletspoc.handlers.delete;

import io.renatofreire.servletspoc.core.RequestHandler;
import io.renatofreire.servletspoc.service.UserService;
import io.renatofreire.servletspoc.utils.PathUtils;
import io.renatofreire.servletspoc.utils.ResponseSenderUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


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