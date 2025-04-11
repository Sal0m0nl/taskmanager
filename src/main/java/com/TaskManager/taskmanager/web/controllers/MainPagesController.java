package com.TaskManager.taskmanager.web.controllers;

import com.TaskManager.taskmanager.domain.security.MyUserDetails;
import com.TaskManager.taskmanager.repository.tables.MyUser;
import com.TaskManager.taskmanager.repository.tables.Task;
import com.TaskManager.taskmanager.web.services.DataBase.DBTaskService;
import com.TaskManager.taskmanager.web.services.DataBase.DBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainPagesController {

    @Autowired
    private DBTaskService dbTaskService;

    @Autowired
    private DBUserService dbUserService;

    @GetMapping("/tasks")
    public String tasksPage(@AuthenticationPrincipal MyUserDetails userDetails,
                            Model model) {

        Iterable<Task> tasks = dbTaskService.getAllTasksById(dbUserService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + userDetails.getUsername()))
                .getId());

        model.addAttribute("tasks", tasks);

        return "tasks";

    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/add-task")
    public String addTaskPage() {

        return "add-task";

    }

    @GetMapping("/get-tasks/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String getTasks(@PathVariable(name = "id") Long ownerId,
                           Model model) {

        Iterable<Task> tasks = dbTaskService.getAllTasksById(ownerId);

        model.addAttribute("tasks", tasks);
        model.addAttribute("username", dbUserService.findById(ownerId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + ownerId + " not found"))
                .getUsername());

        return "get_tasks";

    }

}
