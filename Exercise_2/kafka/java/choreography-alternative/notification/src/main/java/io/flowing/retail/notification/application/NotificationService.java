package io.flowing.retail.notification.application;

import java.util.UUID;

import org.springframework.stereotype.Component;


@Component
public class NotificationService {

  public void sendingEmail(String recipientName, String update) {
    System.out.println("sending mail to: " + recipientName + " - Subject:  " + update);
  }
}
