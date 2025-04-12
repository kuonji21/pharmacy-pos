package com.pharmacy.pos.service;

import com.pharmacy.pos.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    List<User> findAllUsers();
    
    Optional<User> findUserById(Long id);
    
    Optional<User> findUserByUsername(String username);
    
    User saveUser(User user);
    
    void deleteUser(Long id);
    
    boolean authenticateUser(String username, String password);
}