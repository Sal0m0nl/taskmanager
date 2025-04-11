package com.TaskManager.taskmanager.web.controllers;

import com.TaskManager.taskmanager.domain.security.MyUserDetails;
import com.TaskManager.taskmanager.repository.tables.MyUser;
import com.TaskManager.taskmanager.repository.tables.Task;
import com.TaskManager.taskmanager.web.services.DataBase.DBTaskService;
import com.TaskManager.taskmanager.web.services.DataBase.DBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {

    @Autowired
    private DBUserService dbUserService;

    @Autowired
    private DBTaskService dbTaskService;

    @PostMapping("/register-user")
    public String registerUser(@RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password) {

        MyUser user = new MyUser();

        user.setUsername(username);
        user.setPassword(dbUserService.passwordEncoder(password));
        user.setRoles("ROLE_USER");

        dbUserService.save(user);

        return "User registered successfully";

    }

    @PostMapping("/register-task")
    public String addTask(@AuthenticationPrincipal MyUserDetails userDetails,
                          @RequestParam String name,
                          @RequestParam String description) {

        Task task = new Task();

        task.setName(name);
        task.setDescription(description);
        task.setOwnerId(dbUserService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + userDetails.getUsername()))
                .getId());

        dbTaskService.save(task);

        return "Task added successfully";

    }

}
