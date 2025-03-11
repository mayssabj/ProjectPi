package com.example.forum.Service;

import com.example.forum.Entity.Notification;
import com.example.forum.Repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements INotificationService{

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public List<Notification> retrieveAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> retrieveNotification(Integer id) {
        return notificationRepository.findById(id);
    }

    @Override
    public Notification addNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Integer notification) {
        notificationRepository.deleteById(notification);
    }

    @Override
    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
