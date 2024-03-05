package io.flowing.retail.notification.messages;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.flowing.retail.notification.application.NotificationService;
import io.flowing.retail.notification.messages.payload.CustomerPayload;
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
    Message<JsonNode> message = objectMapper.readValue(messageJson, new TypeReference<>() {
    });
    ObjectNode payload = (ObjectNode) message.getData();
    CustomerPayload customer = objectMapper.treeToValue(payload.get("customer"), CustomerPayload.class);
    String subject = null;
    switch (messageType) {
      case "OrderPlacedEvent"-> subject = "Order placed";
      case "PaymentReceivedEvent" -> subject = "Payment received";
      case "GoodsFetchedEvent" -> subject = "Goods fetched";
      case "GoodsShippedEvent"-> subject =  "Goods shipped";
      case "OrderCompletedEvent" -> subject =  "Order completed";
      case "GoodsNotFetchedEvent" -> subject ="Not enough Goods";
      case "PaymentRefundedEvent" -> subject = "Payment refunded";
      default -> System.out.println("Unknown message type");
    }
    notificationService.sendingEmail(customer,subject);
  }

  @Autowired
  private ObjectMapper objectMapper;

    
}
