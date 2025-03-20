package com.example.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Notification;
import com.example.enums.NotificationType;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByType(NotificationType type, Pageable pageable);
    
    Page<Notification> findByRead(boolean read, Pageable pageable);
}
