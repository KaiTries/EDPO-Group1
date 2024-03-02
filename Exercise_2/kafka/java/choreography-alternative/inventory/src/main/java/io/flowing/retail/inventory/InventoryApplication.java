package io.flowing.retail.inventory;

import com.fasterxml.jackson.databind.JsonNode;
import io.flowing.retail.inventory.domain.Inventory;
import io.flowing.retail.inventory.messages.Message;
import io.flowing.retail.inventory.messages.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(InventoryApplication.class, args);
  }
}
