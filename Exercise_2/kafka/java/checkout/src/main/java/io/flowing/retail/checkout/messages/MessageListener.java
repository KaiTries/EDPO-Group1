package io.flowing.retail.checkout.messages;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import io.flowing.retail.checkout.domain.Inventory;
import io.flowing.retail.checkout.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class MessageListener {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOPIC = "flowing-retail";

    @Transactional
    @KafkaListener(id = "checkout", topics = TOPIC)
    public void inventoryStatus(String messageJson, @Header("type") String messageType) throws IOException {
        if ("InventoryStatusEvent".equals(messageType)) {
            Message<JsonNode> message = objectMapper.readValue(messageJson, new TypeReference<Message<JsonNode>>() {
            });

            Item[] items = objectMapper.treeToValue(message.getData(), Item[].class);

            Inventory.getInstance().setInventory(items);
        }
    }
}
