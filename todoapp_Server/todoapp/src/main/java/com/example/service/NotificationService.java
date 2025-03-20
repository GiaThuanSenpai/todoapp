package com.example.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.entities.Notification;
import com.example.entities.Task;
import com.example.enums.NotificationType;
import com.example.enums.Status;
import com.example.repository.NotificationRepository;
import com.example.repository.TaskRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Page<Notification> getNotifications(NotificationType type, Pageable pageable) {
        if (type != null) {
            return notificationRepository.findByType(type, pageable);
        }
        return notificationRepository.findAll(pageable);
    }

    public void createUpcomingNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayFromNow = now.plusDays(1);
        
        List<Task> upcomingTasks = taskRepository.findByDueDateBetweenAndStatusNot(
            now, dayFromNow, Status.COMPLETED);
        
        for (Task task : upcomingTasks) {
            Notification notification = new Notification();
            notification.setTask(task);
            notification.setType(NotificationType.UPCOMING);
            notification.setMessage("Task '" + task.getTitle() + "' is due soon");
            notificationRepository.save(notification);
        }
    }

    public void createOverdueNotifications() {
        LocalDateTime now = LocalDateTime.now();
        
        List<Task> overdueTasks = taskRepository.findByDueDateBeforeAndStatusNot(now, Status.COMPLETED);
        
        for (Task task : overdueTasks) {
            Notification notification = new Notification();
            notification.setTask(task);
            notification.setType(NotificationType.OVERDUE);
            notification.setMessage("Task '" + task.getTitle() + "' is overdue");
            notificationRepository.save(notification);
        }
    }
}

