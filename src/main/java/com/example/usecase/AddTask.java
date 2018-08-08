package com.example.usecase;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Value.Enclosing
@Secured("ROLE_ADMIN")
public class AddTask {
    private TaskRepository repository;

    @Autowired
    public AddTask(TaskRepository repository) {
        this.repository = repository;
    }

    public Response run(Request request) {
        if (Objects.isNull(request)) {
            return  ImmutableAddTask.Response.of(Status.REQUEST_ERROR);
        }

        if (Objects.nonNull(repository.findByNoTask(request.noTask()))){
            return ImmutableAddTask.Response.of(Status.TASK_ALREADY_EXISTS);
        }

        Task taskSaved = repository.save(from(request));

        return Objects.isNull(taskSaved) ? ImmutableAddTask.Response.of(Status.SAVE_TASK_ERROR) :
               ImmutableAddTask.Response.builder()
                    .task(taskSaved)
                    .status(Status.SAVE_TASK_SUCCESS)
                    .build();
    }


    private Task from(Request request) {
        Task task = new Task();
        task.setDescription(request.description());
        task.setNoTask(request.noTask());
        task.setAssignee(request.assignee());
        return  task;
    }

    @Value.Immutable
    @JsonSerialize(as = ImmutableAddTask.Request.class)
    @JsonDeserialize(as = ImmutableAddTask.Request.class)
    public interface Request {
         @Value.Parameter
         String noTask();

         @Value.Parameter
         String description();

         @Value.Parameter
         String assignee();
    }

    @Value.Immutable
    @JsonSerialize(as = ImmutableAddTask.Response.class)
    @JsonDeserialize(as = ImmutableAddTask.Response.class)
    public interface Response {
        @Value.Parameter
        Status status();

        Optional<Task> task();
    }

    public enum Status {
        REQUEST_ERROR,
        SAVE_TASK_ERROR,
        TASK_ALREADY_EXISTS,
        SAVE_TASK_SUCCESS,
    }
}
