package com.stefanobini.taskmanager.dto;

import com.stefanobini.taskmanager.entity.TaskStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record TaskFilter(
        String title,
        TaskStatus status,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dueBefore,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dueAfter,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime createdBefore,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime createdAfter,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime updatedBefore,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime updatedAfter
) {
}
