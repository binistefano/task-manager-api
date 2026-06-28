package com.stefanobini.taskmanager.service;

import com.stefanobini.taskmanager.dto.TaskRequest;
import com.stefanobini.taskmanager.dto.TaskResponse;
import com.stefanobini.taskmanager.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);

    Page<TaskResponse> getAllTasks(Pageable pageable);

    TaskResponse getTaskById(Long id);

    TaskResponse updateTask(Long id, TaskRequest request);

    void deleteTask(Long id);

    List<TaskResponse> getTasksByStatus(TaskStatus status);
}