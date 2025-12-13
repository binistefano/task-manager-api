package com.stefanobini.taskmanager.service;

import com.stefanobini.taskmanager.dto.TaskRequest;
import com.stefanobini.taskmanager.dto.TaskResponse;
import com.stefanobini.taskmanager.entity.Task;
import com.stefanobini.taskmanager.entity.TaskStatus;
import com.stefanobini.taskmanager.exception.TaskNotFoundException;
import com.stefanobini.taskmanager.repository.TaskRepository;
import com.stefanobini.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_ShouldReturnTaskResponse_WhenValidRequest() {
        TaskRequest request = new TaskRequest(
                "Test Task",
                "Description",
                TaskStatus.TODO,
                LocalDateTime.now().plusDays(1)
        );

        Task savedTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .status(TaskStatus.TODO)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals(savedTask.getId(), response.id());
        assertEquals("Test Task", response.title());

        // Verify repository was actually called exactly once
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        Task task = Task.builder().id(1L).title("Existing Task").build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(1L);

        assertEquals(1L, response.id());
        assertEquals("Existing Task", response.title());
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));
    }
}