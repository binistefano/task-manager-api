package com.stefanobini.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data                  // Lombok: Generates getters, setters, toString, equals, hashcode
@NoArgsConstructor     // JPA requires a no-args constructor
@AllArgsConstructor    // Useful for testing/building objects
@Builder               // Design Pattern: So I can create objects cleanly
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING) // By default JPA saves Enums as numbers, but I want to save the status as string in DB
    private TaskStatus status;

    private LocalDateTime dueDate;

    @CreationTimestamp // Hibernate auto-fills on insert
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Hibernate auto-updates on every save
    private LocalDateTime updatedAt;
}