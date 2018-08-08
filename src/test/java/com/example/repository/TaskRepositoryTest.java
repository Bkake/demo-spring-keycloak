package com.example.repository;

import com.example.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    TaskRepository repository;

    @Test
    public void repositorySavesTaskTest() {
        Task taskActual = new Task();
        taskActual.setDescription("description test");
        taskActual.setAssignee("assignee tes");

        Task taskExcepted = repository.save(taskActual);

        assertEquals(taskExcepted.getDescription(), "description test");
        assertEquals(taskExcepted.getAssignee(), "assignee tes");
    }

    @Test
    public void repositoryFindOneTaskTest() {
        Task task = repository.findOne(1L);

        assertEquals(task.getDescription(), "dev ui with reactJS");
        assertEquals(task.getAssignee(), "Bkake");
    }

    @Test
    public void repositoryFindAllTaskTest() {
        List<Task> tasks = repository.findAll();

        assertEquals(tasks.isEmpty(), false);
    }

    @Test
    public void repositoryFindByNoTaskTest() {
        Task task = repository.findByNoTask("arch");

        assertEquals(task.getDescription(), "define micro-services architecture");
        assertEquals(task.getAssignee(), "Bkake");
    }
}
