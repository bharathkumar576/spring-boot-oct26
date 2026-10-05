package com.mahendra.bootdemo1;

import org.springframework.stereotype.Component;

// Named beans allow us to specify a unique identifier for each component.
@Component("sms")   
public class SmsNotification implements NotificationSender {
    @Override
    public void send() {
        System.out.println("SMS sent!");
    }
}
