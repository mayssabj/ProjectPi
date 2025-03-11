package com.example.forum.Controller;

import com.example.forum.Entity.Notification;
import com.example.forum.Entity.Post;
import com.example.forum.Entity.Reaction;
import com.example.forum.Repository.NotificationRepository;
import com.example.forum.Service.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/notification")
public class NotificationRestController {

    @Autowired
    INotificationService notificationService;

    @GetMapping("/get-all-notification")
    public List<Notification> listAllNotifications(){
        return notificationService.retrieveAllNotifications();
    }

    @GetMapping("/display-notification/{notification-id}")
    public Optional<Notification> displayNotification(@PathVariable("notification-id") Integer id){
        return notificationService.retrieveNotification(id);
    }

    @DeleteMapping("/delete-notification/{notification-id}")
    public  void deleteNotification(@PathVariable ("notification-id")Integer id){
        notificationService.deleteNotification(id);
    }


}
