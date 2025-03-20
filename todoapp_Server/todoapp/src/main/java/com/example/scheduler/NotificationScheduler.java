package com.example.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.service.NotificationService;

@Component
public class NotificationScheduler {

    @Autowired
    private NotificationService notificationService;

    // Run every hour
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void scheduleNotifications() {
        notificationService.createUpcomingNotifications();
        notificationService.createOverdueNotifications();
    }
}
