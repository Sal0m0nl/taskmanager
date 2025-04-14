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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainPagesController {

    @Autowired
    private DBTaskService dbTaskService;

    @Autowired
    private DBUserService dbUserService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal MyUserDetails userDetails) {

        if(userDetails == null)
            return "main";

        return "redirect:/tasks";

    }

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

    @GetMapping("/error/{error}")
    public String errorPage(@PathVariable(name = "error") int error, Model model) {

        switch (error) {
            case 400:
                model.addAttribute("error", "Passwords doesn't match");
                break;
            case 401:
                model.addAttribute("error", "You need to be logged in");
                break;
            case 404:
                model.addAttribute("error", "There is no task with this id");
                break;
        }

        return "error";
    }




    @PostMapping("/remove-task/{id}")
    public String removeTask(@AuthenticationPrincipal MyUserDetails userDetails,
                             @PathVariable(name = "id") Long id) {

        if(userDetails == null)
        {
            return "redirect:/error/401";
        }

        if(dbTaskService.findById(id).isEmpty())
        {
            return "redirect:/error/404";
        }

        dbTaskService.deleteById(id);

        return "redirect:/tasks";

    }

    @PostMapping("/register")
    public String registerUser(@RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "re-password") String rePassword) {

        if(!password.equals(rePassword)) {
            return "redirect:/error/400";
        }

        MyUser user = new MyUser();

        user.setUsername(username);
        user.setPassword(dbUserService.passwordEncoder(password));
        user.setRoles("ROLE_USER");

        dbUserService.save(user);

        return "redirect:/tasks";

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

        return "redirect:/tasks";

    }

}
