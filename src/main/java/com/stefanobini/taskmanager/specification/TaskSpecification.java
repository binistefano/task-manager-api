package com.stefanobini.taskmanager.specification;

import com.stefanobini.taskmanager.entity.Task;
import com.stefanobini.taskmanager.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class TaskSpecification {

    private TaskSpecification() {
    }

    public static Specification<Task> hasStatus(TaskStatus status) {

        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("status"),
                    status
            );
        };
    }

    public static Specification<Task> titleContains(String title) {

        return (root, query, cb) -> {
            if (title == null || title.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Task> dueBefore(LocalDateTime dueBefore) {
        return (root, query, cb) -> {
            if (dueBefore == null) {
                return cb.conjunction();
            }

            return cb.lessThan(
                    root.get("dueDate"),
                    dueBefore
            );
        };
    }

    public static Specification<Task> dueAfter(LocalDateTime dueAfter) {
        return (root, query, cb) -> {
            if (dueAfter == null) {
                return cb.conjunction();
            }

            return cb.greaterThan(
                    root.get("dueDate"),
                    dueAfter
            );
        };
    }

    public static Specification<Task> createdBefore(LocalDateTime createdBefore) {
        return (root, query, cb) -> {
            if (createdBefore == null) {
                return cb.conjunction();
            }

            return cb.lessThan(
                    root.get("createdAt"),
                    createdBefore
            );
        };
    }

    public static Specification<Task> createdAfter(LocalDateTime createdAfter) {
        return (root, query, cb) -> {
            if (createdAfter == null) {
                return cb.conjunction();
            }

            return cb.greaterThan(
                    root.get("createdAt"),
                    createdAfter
            );
        };
    }

    public static Specification<Task> updatedBefore(LocalDateTime updatedBefore) {
        return (root, query, cb) -> {
            if (updatedBefore == null) {
                return cb.conjunction();
            }

            return cb.lessThan(
                    root.get("updatedAt"),
                    updatedBefore
            );
        };
    }

    public static Specification<Task> updatedAfter(LocalDateTime updatedAfter) {
        return (root, query, cb) -> {
            if (updatedAfter == null) {
                return cb.conjunction();
            }

            return cb.greaterThan(
                    root.get("updatedAt"),
                    updatedAfter
            );
        };
    }

}