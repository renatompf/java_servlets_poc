package io.renatofreire.servletspoc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renatofreire.servletspoc.dao.UserDAO;
import io.renatofreire.servletspoc.dto.UserDtoIn;
import io.renatofreire.servletspoc.model.User;
import io.renatofreire.servletspoc.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserService userService;

    private Validator validator;

    @Override
    public void init() {
        LOGGER.log(Level.INFO, "Initializing UserServlet...");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        this.userService = new UserService(new UserDAO());
        this.validator = factory.getValidator();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setupResponse(response);

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<User> users = userService.getAllUsers();
            response.getWriter().write(objectMapper.writeValueAsString(users));
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Optional<User> user = userService.getUserById(id);
                if (user.isPresent()) {
                    response.getWriter().write(objectMapper.writeValueAsString(user.get()));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"User not found\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid user ID format\"}");
            }
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setupResponse(response);
        try {
            if (request.getPathInfo() != null && !request.getPathInfo().equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid endpoint for POST\"}");
                return;
            }

            UserDtoIn dtoIn = objectMapper.readValue(request.getReader(), UserDtoIn.class);
            if(!validateInput(dtoIn, response)){
                return;
            }

            User user = new User(dtoIn.name(), dtoIn.email(), dtoIn.country());

            userService.createUser(user);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\": \"User created successfully\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid user data\"}");
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setupResponse(response);
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                UserDtoIn dtoIn = objectMapper.readValue(request.getReader(), UserDtoIn.class);
                if(!validateInput(dtoIn, response)){
                    return;
                }

                User user = new User(id, dtoIn.name(), dtoIn.email(), dtoIn.country());
                userService.updateUser(user);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"User updated successfully\"}");
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid user ID format\"}");
            }
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setupResponse(response);
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                if (userService.deleteUser(id)) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"User not found\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid user ID format\"}");
            }
        }
    }

    private <T> boolean validateInput(T input, HttpServletResponse response) throws IOException {
        Set<ConstraintViolation<T>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            LOGGER.log(Level.WARNING, "Validation errors: {0}", violations);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(violations));
            return false;
        }
        return true;
    }

    private void setupResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}