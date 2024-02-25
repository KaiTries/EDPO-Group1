package com.examples.consumerLag;

import com.google.common.io.Resources;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Properties;

public class ConsumerLagConsumer {

    public static void main(String[] args) throws IOException {

        int counter = 0;

        KafkaConsumer<String, String> consumer;
        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            consumer = new KafkaConsumer<>(properties);
        }

        /*
            consumer-lag-events are created between each 300ms
            the processing of each event takes artificially 700ms
         */

        consumer.subscribe(Arrays.asList("consumer-lag-events"));

        while (true) {

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5));

            for (ConsumerRecord<String, String> record : records) {
                switch (record.topic()) {
                    case "consumer-lag-events":
                        try {
                            Thread.sleep(700);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        if(Integer.parseInt(String.valueOf(record.value())) > ++counter){
                           System.out.println("data loss occurred...");
                           counter = Integer.parseInt(String.valueOf(record.value()));
                        }
                        System.out.println("consumer-lag-event consumed: " + String.valueOf(record.value()));
                        break;
                    default:
                        throw new IllegalStateException("Shouldn't be possible to get message on topic " + record.topic());
                }
            }
        }
    }
}