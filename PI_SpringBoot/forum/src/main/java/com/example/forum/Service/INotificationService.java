package com.example.forum.Service;

import com.example.forum.Entity.Notification;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    public List<Notification> retrieveAllNotifications();
    public Optional<Notification> retrieveNotification(Integer id);
    public Notification addNotification(Notification notification);
    public void deleteNotification(Integer notification);
    public Notification updateNotification(Notification notification);
}
