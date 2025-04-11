package com.TaskManager.taskmanager.repository;

import com.TaskManager.taskmanager.repository.tables.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUsername(String username);

}
