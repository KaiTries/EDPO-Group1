package io.flowing.retail.payment.messages;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.handler.annotation.Header;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.flowing.retail.payment.application.PaymentService;

@Component
public class MessageListener {    
  
  @Autowired
  private MessageSender messageSender;
  
  @Autowired
  private PaymentService paymentService;

  @Autowired
  private ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(id = "payment", topics = MessageSender.TOPIC_NAME)
  public void orderPlaced(String messageJson, @Header("type") String messageType) throws Exception {
    // Note that we now have to read order data from this message!
    // Bad smell 1 (reading some event instead of dedicated command)
    JsonNode message = objectMapper.readTree(messageJson);
    ObjectNode payload = (ObjectNode) message.get("data");
    String orderId = payload.get("orderId").asText();
    if ("OrderPlacedEvent".equals(messageType)) {
      if (orderId == null) {
        // We do not yet have an order id - as the responsibility who creates that is unclear
        // Bad smell 2 (order context missing)
        // But actually not that problematic - as a good practice could be to
        // generate it in the checkout anyway to improve idempotency
        orderId = UUID.randomUUID().toString();
        payload.put("orderId", orderId);
      }

      long amount = payload.get("totalSum").asLong();

      String paymentId = paymentService.createPayment(orderId, amount);

      // Note that we need to pass along the whole order object
      // Maybe with additional data we have
      // Bad smell 4 (data flow passing through - data might grow big and most data is not needed for payment)
      payload.put("paymentId", paymentId);

      messageSender.send( //
              new Message<JsonNode>( //
                      "PaymentReceivedEvent", //
                      message.get("traceid").asText(), //
                      message.get("data")));
    }
    if ("GoodsNotFetchedEvent".equals(messageType)) {

      long amount = payload.get("totalSum").asLong();

      String refundId = paymentService.refundPayment(orderId, amount);

      // Note that we need to pass along the whole order object
      // Maybe with additional data we have
      // Bad smell 4 (data flow passing through - data might grow big and most data is not needed for payment)
      payload.put("refundId", refundId);

      messageSender.send(
              new Message<JsonNode>( //
                      "PaymentRefundedEvent", //
                      message.get("traceid").asText(), //
                      message.get("data")));
    }
  }
    
}
