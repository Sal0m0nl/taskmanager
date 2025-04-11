package com.TaskManager.taskmanager.repository;

import com.TaskManager.taskmanager.repository.tables.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
