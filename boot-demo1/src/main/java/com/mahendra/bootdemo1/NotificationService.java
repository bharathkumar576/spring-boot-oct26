package com.mahendra.bootdemo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class NotificationService {

    private NotificationSender notificationSender;

    @Autowired
    // Qualifier specifies which bean to inject when multiple beans of the same type exist.
    @Qualifier("email") 
    public void setNotificationSender(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void notifyUser() {
        notificationSender.send();
    }
}
