package io.renatofreire.servletspoc.controllers;

import io.renatofreire.servletspoc.core.BaseApiServlet;
import io.renatofreire.servletspoc.core.HandlerRegistry;
import io.renatofreire.servletspoc.core.RequestHandler;
import io.renatofreire.servletspoc.dao.UserDAO;
import io.renatofreire.servletspoc.handlers.delete.DeleteUserHandler;
import io.renatofreire.servletspoc.handlers.get.GetAllUsersHandler;
import io.renatofreire.servletspoc.handlers.get.GetUserByIdHandler;
import io.renatofreire.servletspoc.handlers.post.CreateUserHandler;
import io.renatofreire.servletspoc.handlers.put.UpdateUserHandler;
import io.renatofreire.servletspoc.service.UserService;
import io.renatofreire.servletspoc.utils.ResponseSenderUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HttpMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/users/*")
public class UserServlet extends BaseApiServlet {

    private UserService userService;
    private HandlerRegistry handlerRegistry;

    @Override
    public void init() {
        logger.info("Initializing UserServlet...");
        this.userService = new UserService(new UserDAO());
        this.handlerRegistry = new HandlerRegistry();

        // Register routes in the registry
        handlerRegistry.register(HttpMethod.GET, "/", new GetAllUsersHandler(userService));
        handlerRegistry.register(HttpMethod.GET, "/id", new GetUserByIdHandler(userService));

        handlerRegistry.register(HttpMethod.POST, "/", new CreateUserHandler(userService, objectMapper, validator));

        handlerRegistry.register(HttpMethod.PUT, "/id", new UpdateUserHandler(userService, objectMapper, validator));

        handlerRegistry.register(HttpMethod.DELETE, "/id", new DeleteUserHandler(userService));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatch(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatch(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatch(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatch(request, response);
    }


    private void dispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = Optional.ofNullable(request.getPathInfo()).orElse("/");
        RequestHandler handler;

        if ("/".equals(path)) {
            handler = handlerRegistry.getHandler(request.getMethod(), "/");
        } else if (path.matches("/\\d+")) {
            handler = handlerRegistry.getHandler(request.getMethod(), "/id");
        } else {
            ResponseSenderUtils.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Unknown endpoint");
            return;
        }

        if (handler != null) {
            handler.handle(request, response);
        } else {
            ResponseSenderUtils.sendError(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Handler not implemented");
        }
    }
}