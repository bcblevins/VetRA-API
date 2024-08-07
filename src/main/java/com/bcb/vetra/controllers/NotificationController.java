package com.bcb.vetra.controllers;

import com.bcb.vetra.models.Notification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.bcb.vetra.daos.NotificationDao;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@CrossOrigin
@RequestMapping("/notifications")
public class NotificationController {
    private NotificationDao notificationDao;

    public NotificationController(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @GetMapping
    public List<Notification> getAll(Principal principal) {
        return notificationDao.getNotificationsByUsername(principal.getName());
    }

    @PostMapping
    public void markAsRead(@RequestBody int id, Principal principal) {
        notificationDao.markAsRead(id, principal.getName());
    }

}
