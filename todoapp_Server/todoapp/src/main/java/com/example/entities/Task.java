package com.example.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.enums.Priority;
import com.example.enums.Status;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_priority", columnList = "priority"),
    @Index(name = "idx_due_date", columnList = "due_date"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    private String title;

    private String description;

    @Column(name = "due_date") // Đổi tên tránh lỗi SQL
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority") // Đảm bảo có cột rõ ràng
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.TODO;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
