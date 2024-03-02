package io.flowing.retail.inventory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.inventory.domain.Inventory;
import io.flowing.retail.inventory.messages.Message;
import io.flowing.retail.inventory.messages.MessageSender;
import org.springframework.aop.config.AbstractInterceptorDrivenBeanDefinitionDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyStartupRunner implements ApplicationRunner {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) {

        System.out.println(Inventory.getInstance().getInventory().size());

        messageSender.send(
            new Message<JsonNode>(
                "InventoryStatusEvent",
                objectMapper.valueToTree(Inventory.getInstance().getInventory())));
    }
}