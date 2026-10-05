package com.mahendra.bootdemo1;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("email")
public class EmailNotification implements NotificationSender {
    @Override
    public void send() {
        System.out.println("Email sent!");
    }
}
