package com.stefanobini.taskmanager.mapper;

import com.stefanobini.taskmanager.dto.TaskRequest;
import com.stefanobini.taskmanager.dto.TaskResponse;
import com.stefanobini.taskmanager.entity.Task;
import com.stefanobini.taskmanager.entity.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public Task toEntity(TaskRequest request){
        return Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status() == null ? TaskStatus.TODO : request.status())
                .dueDate(request.dueDate())
                .build();
    }
}