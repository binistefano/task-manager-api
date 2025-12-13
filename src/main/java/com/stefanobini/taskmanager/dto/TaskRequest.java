package com.stefanobini.taskmanager.dto;

import com.stefanobini.taskmanager.entity.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskRequest(

        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @Size(max = 500, message = "Description is too long")
        String description,

        TaskStatus status,

        @FutureOrPresent(message = "Due date cannot be in the past")
        LocalDateTime dueDate
) {
}