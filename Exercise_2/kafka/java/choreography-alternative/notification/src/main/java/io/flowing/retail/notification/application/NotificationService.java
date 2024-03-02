package io.flowing.retail.notification.application;

import io.flowing.retail.notification.messages.payload.CustomerPayload;
import org.springframework.stereotype.Component;


@Component
public class NotificationService {

  public void sendingEmail(CustomerPayload customerPayload, String update) {
    if (update != null) {
    System.out.println("sending mail to: " + customerPayload.getEmail() + " - Subject:  " + update);
    }
  }
}
