package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UsernameDTO;

import java.util.List;

public interface UserDao {

    List<User> findAll();
    List<UsernameDTO> findAllUsers();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
