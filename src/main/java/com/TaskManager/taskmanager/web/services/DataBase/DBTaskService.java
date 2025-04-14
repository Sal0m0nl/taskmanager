package com.TaskManager.taskmanager.web.services.DataBase;

import com.TaskManager.taskmanager.repository.TaskRepository;
import com.TaskManager.taskmanager.repository.tables.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DBTaskService {

    @Autowired
    TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasksById(Long ownerId) {

        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getOwnerId().equals(ownerId)).collect(Collectors.toList());

    }

}
