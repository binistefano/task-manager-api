package com.stefanobini.taskmanager.config;

import com.stefanobini.taskmanager.entity.TaskStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "task")
public record TaskProperties(
        @NotNull
        TaskStatus defaultStatus,
        @Min(1)
        int maxPageSize
) {
}