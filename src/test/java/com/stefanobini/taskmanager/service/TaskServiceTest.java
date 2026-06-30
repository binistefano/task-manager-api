package com.stefanobini.taskmanager.service;

import com.stefanobini.taskmanager.dto.TaskRequest;
import com.stefanobini.taskmanager.dto.TaskResponse;
import com.stefanobini.taskmanager.entity.Task;
import com.stefanobini.taskmanager.entity.TaskStatus;
import com.stefanobini.taskmanager.exception.TaskNotFoundException;
import com.stefanobini.taskmanager.mapper.TaskMapper;
import com.stefanobini.taskmanager.repository.TaskRepository;
import com.stefanobini.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_ShouldReturnTaskResponse_WhenValidRequest() {
        // Arrange
        LocalDateTime dueDate = LocalDateTime.of(2030, 7, 15, 18, 0);
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 30, 12, 0);

        String title = "Test Task";
        String description = "Description";
        TaskStatus status = TaskStatus.TODO;

        TaskRequest request = new TaskRequest(
                title,
                description,
                status,
                dueDate
        );

        Task taskEntity = Task.builder()
                .title(title)
                .description(description)
                .status(status)
                .dueDate(dueDate)
                .build();

        Task savedTask = Task.builder()
                .id(1L)
                .title(title)
                .description(description)
                .status(status)
                .dueDate(dueDate)
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();

        TaskResponse expectedResponse = new TaskResponse(
                1L,
                title,
                description,
                status,
                dueDate,
                createdAt,
                createdAt
        );

        when(taskMapper.toEntity(request)).thenReturn(taskEntity);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toResponse(savedTask)).thenReturn(expectedResponse);

        // Act
        TaskResponse actual = taskService.createTask(request);

        // Assert
        assertEquals(expectedResponse, actual);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(taskCaptor.capture());

        Task persistedTask = taskCaptor.getValue();

        assertEquals(title, persistedTask.getTitle());
        assertEquals(description, persistedTask.getDescription());
        assertEquals(status, persistedTask.getStatus());
        assertEquals(dueDate, persistedTask.getDueDate());

        verify(taskMapper).toEntity(request);
        verify(taskMapper).toResponse(savedTask);
        verifyNoMoreInteractions(taskMapper, taskRepository);
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        long id = 1L;
        String title = "Existing task";
        String description = "Description";
        TaskStatus status = TaskStatus.TODO;
        LocalDateTime dueDate = LocalDateTime.of(2030, 7, 15, 18, 0);
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 30, 12, 0);

        Task task = Task.builder().id(id).title(title).build();
        TaskResponse expectedResponse = new TaskResponse(
                id, title, description, status, dueDate, createdAt, createdAt
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(expectedResponse);

        TaskResponse response = taskService.getTaskById(1L);

        assertEquals(id, response.id());
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));
    }
}