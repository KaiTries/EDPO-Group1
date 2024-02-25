package com.examples.consumerGroups;

import com.data.Cat;
import com.google.common.io.Resources;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerCat {

    public static void main(String[] args) {

        KafkaConsumer<String, Cat> consumerCat;
        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            consumerCat = new KafkaConsumer<>(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        consumerCat.subscribe(Arrays.asList("cats"));

        while (true) {

            ConsumerRecords<String, Cat> records = consumerCat.poll(Duration.ofMillis(200));

            for (ConsumerRecord<String, Cat> record : records) {
                switch (record.topic()) {
                    case "cats":
                        String value = String.valueOf(record.value());
                        System.out.println("cats consumed: " + value);
                        break;
                    default:
                        throw new IllegalStateException("Shouldn't be possible to get message on topic " + record.topic());
                }
            }
        }
    }
}