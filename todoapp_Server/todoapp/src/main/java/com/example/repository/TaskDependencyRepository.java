package com.example.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.TaskDependency;

@Repository
public interface TaskDependencyRepository extends JpaRepository<TaskDependency, UUID> {
    List<TaskDependency> findByTaskId(UUID taskId);
    
    List<TaskDependency> findByDependsOnTaskId(UUID dependsOnTaskId);
    
    void deleteByTaskIdAndDependsOnTaskId(UUID taskId, UUID dependsOnTaskId);
    
    boolean existsByTaskIdAndDependsOnTaskId(UUID taskId, UUID dependsOnTaskId);
}
