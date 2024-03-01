package io.flowing.retail.notification.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.flowing.retail.notification.application.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.handler.annotation.Header;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class MessageListener {

  public static final String TOPIC_NAME = "flowing-retail";

  @Autowired
  private NotificationService notificationService;

  @Transactional
  @KafkaListener(id = "notification", topics = TOPIC_NAME)
  public void goodsFetchedEventReceived(String messageJson, @Header("type") String messageType) throws Exception {
    Message<JsonNode> message = objectMapper.readValue(messageJson, new TypeReference<Message<JsonNode>>() {
    });
    ObjectNode payload = (ObjectNode) message.getData();
    String customer = payload.get("customer").get("name").asText();

    switch (messageType) {
      case "OrderPlacedEvent":
        notificationService.sendingEmail(customer, "Order placed");
        break;
      case "PaymentReceivedEvent":
        notificationService.sendingEmail(customer, "Payment received");
        break;
      case "GoodsFetchedEvent":
        notificationService.sendingEmail(customer, "Goods fetched");
        break;
      case "GoodsShippedEvent":
        notificationService.sendingEmail(customer, "Goods shipped");
        break;
      case "OrderCompletedEvent":
        notificationService.sendingEmail(customer, "Order completed");
        break;
      default:
        System.out.println("Unknown message type");
        break;
    }


  }

  @Autowired
  private ObjectMapper objectMapper;

    
}
