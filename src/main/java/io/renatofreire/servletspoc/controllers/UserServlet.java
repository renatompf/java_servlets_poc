package io.renatofreire.servletspoc.controllers;

import io.renatofreire.servletspoc.controllers.requestHandlers.RequestHandler;
import io.renatofreire.servletspoc.controllers.requestHandlers.user.*;
import io.renatofreire.servletspoc.dao.UserDAO;
import io.renatofreire.servletspoc.service.UserService;
import io.renatofreire.servletspoc.utils.ResponseSenderUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/users/*")
public class UserServlet extends BaseApiServlet {

    private UserService userService;
    private final Map<String, RequestHandler> getHandlers = new HashMap<>();
    private final Map<String, RequestHandler> postHandlers = new HashMap<>();
    private final Map<String, RequestHandler> putHandlers = new HashMap<>();
    private final Map<String, RequestHandler> deleteHandlers = new HashMap<>();

    @Override
    public void init() {
        logger.info("Initializing UserServlet...");
        this.userService = new UserService(new UserDAO());

        // Register routes
        getHandlers.put("/", new GetAllUsersHandler(userService));
        getHandlers.put("/id", new GetUserByIdHandler(userService));

        postHandlers.put("/", new CreateUserHandler(userService, objectMapper, validator));

        putHandlers.put("/id", new UpdateUserHandler(userService, objectMapper, validator));

        deleteHandlers.put("/id", new DeleteUserHandler(userService));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatch(request, response, getHandlers);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatch(request, response, postHandlers);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatch(request, response, putHandlers);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatch(request, response, deleteHandlers);
    }

    private void dispatch(HttpServletRequest request, HttpServletResponse response, Map<String, RequestHandler> handlers) throws IOException {
        String path = Optional.ofNullable(request.getPathInfo()).orElse("/");
        RequestHandler handler;

        if ("/".equals(path)) {
            handler = handlers.get("/");
        } else if (path.matches("/\\d+")) {
            handler = handlers.get("/id");
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