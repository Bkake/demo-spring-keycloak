package com.example.repository;

import com.example.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByNoTask(String noTask);
}
