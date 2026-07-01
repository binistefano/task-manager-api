package com.stefanobini.taskmanager.dto;

import com.stefanobini.taskmanager.entity.TaskStatus;

public record TaskFilter(
        TaskStatus status,
        String title
) {
}
