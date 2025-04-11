package com.TaskManager.taskmanager.web.services.DataBase;

import com.TaskManager.taskmanager.repository.UserRepository;
import com.TaskManager.taskmanager.repository.tables.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DBUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<MyUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<MyUser> findById(Long id) { return userRepository.findById(id); }

    public MyUser save(MyUser user) {
        return userRepository.save(user);
    }

    public String passwordEncoder(String password) {
        return passwordEncoder.encode(password);
    }

}
