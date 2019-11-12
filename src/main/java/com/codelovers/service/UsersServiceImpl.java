package com.codelovers.service;

import com.codelovers.model.Users;
import com.codelovers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Admin on 1/4/2017.
 */
@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Users users) {
        userRepository.delete(users);
    }

    @Transactional
    @Override
    public void save(Users users) {
        userRepository.saveAndFlush(users);
    }
}
