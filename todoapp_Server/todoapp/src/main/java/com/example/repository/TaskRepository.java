package com.example.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Task;
import com.example.enums.Priority;
import com.example.enums.Status;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findByStatus(Status status, Pageable pageable);
    
    Page<Task> findByPriority(Priority priority, Pageable pageable);
    
    Page<Task> findByStatusAndPriority(Status status, Priority priority, Pageable pageable);
    
    List<Task> findByDueDateBeforeAndStatusNot(LocalDateTime date, Status status);
    
    List<Task> findByDueDateBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, Status status);
}

