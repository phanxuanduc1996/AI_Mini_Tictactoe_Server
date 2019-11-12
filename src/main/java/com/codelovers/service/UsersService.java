package com.codelovers.service;

import com.codelovers.model.Users;

import java.util.List;

/**
 * Created by Admin on 1/4/2017.
 */
public interface UsersService {
    Users findById(Long id);

    Users findByUsername(String username);

    List<Users> findAll();

    void save(Users users);

    void delete(Users users);
}
