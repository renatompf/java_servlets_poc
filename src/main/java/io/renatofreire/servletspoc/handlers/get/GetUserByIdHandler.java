package io.renatofreire.servletspoc.handlers.get;

import io.renatofreire.servletspoc.core.RequestHandler;
import io.renatofreire.servletspoc.model.User;
import io.renatofreire.servletspoc.service.UserService;
import io.renatofreire.servletspoc.utils.PathUtils;
import io.renatofreire.servletspoc.utils.ResponseSenderUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class GetUserByIdHandler implements RequestHandler {

    private final UserService userService;

    public GetUserByIdHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Integer id = PathUtils.extractId(request.getPathInfo()).orElseThrow(() -> new NumberFormatException("Invalid ID format"));
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                ResponseSenderUtils.sendSuccess(response, user.get());
            } else {
                ResponseSenderUtils.sendError(response, HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (NumberFormatException e) {
            ResponseSenderUtils.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        }
    }
}