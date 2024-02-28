package com.example;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Producer {

    public static void main(String[] args) throws IOException {
        KafkaProducer<String, String> producer;
        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);
        }

        try {
            for (int i = 0; i < 100000; i++) {
                Thread.sleep(1000);
                producer.send(new ProducerRecord<String, String>(
                      "topic1", // topic
                      "some_value_" + System.nanoTime())); // value
                producer.send(new ProducerRecord<String, String>(
                        "topic2", // topic
                        "some_value_" + System.nanoTime())); // value
                producer.send(new ProducerRecord<String, String>(
                        "topic3", // topic
                        "some_value_" + System.nanoTime())); // value
            }
        } catch (Throwable throwable) {
            System.out.println(throwable.getStackTrace());
        } finally {
            producer.close();
        }

    }
}
