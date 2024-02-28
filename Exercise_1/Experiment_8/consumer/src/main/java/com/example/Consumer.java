package com.example;

import com.google.common.io.Resources;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) throws IOException {
        KafkaConsumer<String, String> consumer;
        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            consumer = new KafkaConsumer<>(properties);
        }

        consumer.subscribe(List.of("topic1","topic2","topic3"));

        int i = 0;

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("topic = %s, partition = %d, offset = %d, key = %s, value = %s%n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    i++;
                }
            }
        } finally {
                consumer.close();
            }
        }

}
