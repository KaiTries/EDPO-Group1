package com.examples.consumerGroups;

import com.data.Dog;
import com.google.common.io.Resources;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDog1 {

    public static void main(String[] args) {

        KafkaConsumer<String, Dog> consumerDog1;
        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            consumerDog1 = new KafkaConsumer<>(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        consumerDog1.subscribe(Arrays.asList("dogs"));

        while (true) {

            ConsumerRecords<String, Dog> records = consumerDog1.poll(Duration.ofMillis(200));

            for (ConsumerRecord<String, Dog> record : records) {
                switch (record.topic()) {
                    case "dogs":
                        String value = String.valueOf(record.value());
                        System.out.println("dogs consumed: " + value);
                        break;
                    default:
                        throw new IllegalStateException("Shouldn't be possible to get message on topic " + record.topic());
                }
            }
        }
    }
}