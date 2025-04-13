package io.renatofreire.servletspoc.controllers.requestHandlers.user;

import io.renatofreire.servletspoc.controllers.requestHandlers.RequestHandler;
import io.renatofreire.servletspoc.service.UserService;
import io.renatofreire.servletspoc.utils.ResponseSenderUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetAllUsersHandler implements RequestHandler {

    private final UserService userService;

    public GetAllUsersHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseSenderUtils.sendSuccess(response, userService.getAllUsers());
    }

}