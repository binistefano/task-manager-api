package com.stefanobini.taskmanager.repository;

import com.stefanobini.taskmanager.entity.Task;
import com.stefanobini.taskmanager.entity.TaskStatus;
import com.stefanobini.taskmanager.specification.TaskSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class TaskRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private TaskRepository repository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url",
                postgres::getJdbcUrl);

        registry.add("spring.datasource.username",
                postgres::getUsername);

        registry.add("spring.datasource.password",
                postgres::getPassword);
    }

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private Task buildTask(String title) {
        return buildTask(title, TaskStatus.IN_PROGRESS);
    }

    private Task buildTask(String title, TaskStatus status) {
        return Task.builder()
                .title(title)
                .description("Description")
                .status(status)
                .dueDate(LocalDateTime.of(2030, 7, 15, 18, 0))
                .build();
    }

    private Task buildTask(String title, TaskStatus status, LocalDateTime dueDate) {
        return Task.builder()
                .title(title)
                .description("Description")
                .status(status)
                .dueDate(dueDate)
                .build();
    }

    @Test
    void hasStatus_ShouldReturnOnlyMatchingTasks() {
        Task todoTask = buildTask("Task in todo", TaskStatus.TODO);
        Task inProgresTask = buildTask("Task in progress", TaskStatus.IN_PROGRESS);
        Task doneTask = buildTask("Task in done", TaskStatus.DONE);

        repository.saveAll(List.of(todoTask, inProgresTask, doneTask));

        Page<Task> result = repository.findAll(
                TaskSpecification.hasStatus(TaskStatus.TODO),
                Pageable.unpaged()
        );

        assertEquals(1, result.getNumberOfElements());

        Task task = result.getContent().getFirst();
        assertEquals("Task in todo", task.getTitle());
        assertEquals(TaskStatus.TODO, task.getStatus());
    }
    @Test
    void titleContains_ShouldReturnMatchingTasks(){
        Task taskDocker1 = buildTask("Add Dockerfile");
        Task taskDocker2 = buildTask("Infastructure: add docker-compose file");
        Task taskNotDocker = buildTask("Add REST endpoints");

        repository.saveAll(List.of(taskDocker1, taskDocker2, taskNotDocker));

        Specification<Task> specification = TaskSpecification.titleContains("doCKer");

        var result = repository.findAll(specification, Pageable.unpaged());

        assertEquals(2, result.getTotalElements());

        assertTrue(
                result.getContent()
                        .stream()
                        .allMatch(task ->
                                task.getTitle().toLowerCase().contains("docker"))
        );
    }

    @Test
    void hasStatusAndTitle_ShouldReturnOnlyMatchingTasks() {
        Task task1 = buildTask("Buy milk", TaskStatus.TODO);
        Task task2 = buildTask("Meeting notes", TaskStatus.TODO);
        Task task3 = buildTask("Buy bread", TaskStatus.DONE);
        Task task4 = buildTask("Deploy Dcoker", TaskStatus.DONE);

        repository.saveAll(List.of(task1, task2, task3, task4));

        Specification<Task> specification = TaskSpecification.hasStatus(TaskStatus.TODO)
                .and(TaskSpecification.titleContains("buy"));

        var result = repository.findAll(specification, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());

        Task matchingTask = result.getContent().getFirst();

        assertEquals(TaskStatus.TODO, matchingTask.getStatus());
        assertEquals("Buy milk", matchingTask.getTitle());

    }

    @Test
    void dueBefore_ShouldReturnMatchingTasks() {
        Task task1 = buildTask(
                "Task",
                TaskStatus.TODO,
                LocalDateTime.of(2030, 1, 1, 15, 15)
        );
        Task task2 = buildTask(
                "Task",
                TaskStatus.TODO,
                LocalDateTime.of(2030, 6, 1, 15, 15)
        );
        Task task3 = buildTask(
                "Task",
                TaskStatus.TODO,
                LocalDateTime.of(2031, 1, 1, 15, 15)
        );

        repository.saveAll(List.of(task1, task2, task3));

        LocalDateTime filterDate = LocalDateTime.of(2030, 7, 1, 15, 15);
        Specification<Task> specification = TaskSpecification.dueBefore(filterDate);

        var result = repository.findAll(specification, Pageable.unpaged());

        assertEquals(2, result.getTotalElements());

        assertTrue(
                result.getContent().stream()
                        .allMatch(task -> task.getDueDate().isBefore(filterDate))
        );
    }

    @Test
    void dueAfter_ShouldReturnMatchingTasks() {
        Task task1 = buildTask(
                "Task",
                TaskStatus.TODO,
                LocalDateTime.of(2030, 1, 1, 15, 15)
        );
        Task task2 = buildTask(
                "Task",
                TaskStatus.TODO,
                LocalDateTime.of(2030, 6, 1, 15, 15)
        );
        Task task3 = buildTask(
                "Task",
                TaskStatus.TODO,
                LocalDateTime.of(2031, 1, 1, 15, 15)
        );

        repository.saveAll(List.of(task1, task2, task3));

        LocalDateTime filterDate = LocalDateTime.of(2030, 3, 1, 15, 15);
        Specification<Task> specification = TaskSpecification.dueAfter(filterDate);

        var result = repository.findAll(specification, Pageable.unpaged());

        assertEquals(2, result.getTotalElements());

        assertTrue(
                result.getContent().stream()
                        .allMatch(task -> task.getDueDate().isAfter(filterDate))
        );
    }
}
