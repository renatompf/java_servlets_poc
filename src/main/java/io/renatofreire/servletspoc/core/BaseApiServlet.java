package io.renatofreire.servletspoc.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.logging.Logger;

public abstract class BaseApiServlet extends HttpServlet {

    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final Validator validator;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    public BaseApiServlet() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

}