package io.renatofreire.servletspoc.service;

import io.renatofreire.servletspoc.dao.UserDAO;
import io.renatofreire.servletspoc.model.User;
import jakarta.persistence.EntityExistsException;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        return userDAO.selectAllUsers();
    }

    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userDAO.selectUserById(id));
    }

    public void createUser(User user) {
        userDAO.insertUser(user);
    }

    public void updateUser(User user) {

        if(userDAO.selectUserByEmail(user.getEmail()) != null) {
            throw new EntityExistsException("User with this email already exists");
        }

        userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) {
        try{
            userDAO.deleteUser(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
