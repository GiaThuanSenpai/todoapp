package com.example.entities;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "task_dependencies", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "depends_on_task_id"}),
       indexes = {
           @Index(name = "idx_task_id", columnList = "task_id"),
           @Index(name = "idx_depends_on_task_id", columnList = "depends_on_task_id")
       })
@Data
public class TaskDependency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID dependencyId;

    @JoinColumn(name = "task_id", nullable = false)
    private UUID taskId;

    @JoinColumn(name = "depends_on_task_id", nullable = false)
    private UUID dependsOnTaskId;
}
