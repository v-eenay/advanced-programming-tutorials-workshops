package com.example.aptutorialworkshop.services;

import com.example.aptutorialworkshop.dao.UserDAO;
import com.example.aptutorialworkshop.models.UserModel;

public class AuthService {
    public static int register(String name, String email, String password, String role, byte[] image){
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(UserModel.Role.valueOf(role));
        user.setImage(image);
        return UserDAO.registerUser(user);
    }

    public static UserModel login(String email, String password){
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setPassword(password);
        return UserDAO.loginUser(user);
    }

    public static UserModel getUserById(int id) {
        return UserDAO.getUserById(id);
    }
}
