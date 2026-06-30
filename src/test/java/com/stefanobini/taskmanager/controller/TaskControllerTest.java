package com.stefanobini.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefanobini.taskmanager.dto.TaskRequest;
import com.stefanobini.taskmanager.dto.TaskResponse;
import com.stefanobini.taskmanager.entity.TaskStatus;
import com.stefanobini.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        String title = "Test Task", description = "Description";
        TaskStatus status = TaskStatus.TODO;
        LocalDateTime dueDate = LocalDateTime.of(2026, 7, 15, 18, 0);
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 30, 12, 0);

        TaskRequest request = new TaskRequest(
                title,
                description,
                status,
                dueDate
        );

        TaskResponse response = new TaskResponse(
                1L,
                title,
                description,
                status,
                dueDate,
                createdAt,
                createdAt
        );


        when(taskService.createTask(any(TaskRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.status").value(status.name()));

        verify(taskService).createTask(any(TaskRequest.class));
    }
}