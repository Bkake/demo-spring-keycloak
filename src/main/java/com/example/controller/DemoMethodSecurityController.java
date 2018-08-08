package com.example.controller;

import com.example.annotation.IsAdmAndUser;
import com.example.annotation.IsAdmin;
import com.example.annotation.IsUser;
import com.example.model.Task;
import com.example.repository.TaskRepository;
import com.example.usecase.AddTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoMethodSecurityController {
    @Autowired
    private AddTask addTask;

    @Autowired
    private TaskRepository repository;

    @PostMapping("/create")
    @IsAdmin
    public AddTask.Response addTask(@RequestBody AddTask.Request request) {
       return addTask.run(request);
    }

    @GetMapping("/findAll")
    @IsAdmAndUser
    public List<Task> findAllTask() {
        return repository.findAll();
    }

    @GetMapping("/{noTask}")
    @IsUser
    public Task findByTask(@PathVariable String noTask) {
        return repository.findByNoTask(noTask);
    }

    @PostMapping("/test-secured-usecase")
    public AddTask.Response addTaskToUseCase(@RequestBody AddTask.Request request) {
        return addTask.run(request);
    }
}
