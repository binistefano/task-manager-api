package com.stefanobini.taskmanager.repository;

import com.stefanobini.taskmanager.entity.Task;
import com.stefanobini.taskmanager.entity.TaskStatus;
import com.stefanobini.taskmanager.specification.TaskSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private Task buildTask(String title, TaskStatus status) {
        return Task.builder()
                .title(title)
                .description("Description")
                .status(status)
                .dueDate(LocalDateTime.of(2030, 7, 15, 18, 0))
                .build();
    }

    @Test
    void findByStatus_ShouldReturnOnlyMatchingTasks() {
        Task todoTask = buildTask("Task in todo", TaskStatus.TODO);
        Task inProgresTask = buildTask("Task in progress", TaskStatus.IN_PROGRESS);
        Task doneTask = buildTask("Task in done", TaskStatus.DONE);

        repository.saveAll(List.of(todoTask, inProgresTask, doneTask));

        Page<Task> result = repository.findAll(
                TaskSpecification.hasStatus(TaskStatus.TODO),
                Pageable.ofSize(5)
        );

        assertEquals(1, result.getNumberOfElements());

        Task task = result.getContent().getFirst();
        assertEquals("Task in todo", task.getTitle());
        assertEquals(TaskStatus.TODO, task.getStatus());
    }
}
