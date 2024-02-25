package com.examples;

import com.data.LatencyPayload;
import com.google.common.io.Resources;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

public class LatencyEventProducer {

    public static void main(String[] args) throws Exception {

        /*
            latency-events are produced at a rate of 200Hz
            They produce a payload with a timestamp. Therefore, upon receiving a latency-event
            the consumer can calculate the difference between the creation and the processing of the event.
         */

        String topic = "latency-events";

        Properties properties;
        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            properties = new Properties();
            properties.load(props);
        }

        KafkaProducer<String, LatencyPayload> producer = new KafkaProducer<>(properties);

        deleteTopic(topic, properties);
        createTopic(topic, 1, properties);

        try {

            int counter = 0;

            while (true) {

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                LatencyPayload latencyPayload = new LatencyPayload(counter);

                producer.send(new ProducerRecord<>(topic, latencyPayload));

                System.out.println("latencyEvent sent: " + latencyPayload);

                counter++;

            }

        } catch (Throwable throwable) {
            System.out.println(throwable.getStackTrace());
        } finally {
            producer.close();
        }
    }

    /*
    Create topic
     */
    private static void createTopic(String topicName, int numPartitions, Properties properties) throws Exception {

        AdminClient admin = AdminClient.create(properties);

        boolean alreadyExists = admin.listTopics().names().get().stream()
                .anyMatch(existingTopicName -> existingTopicName.equals(topicName));
        if (alreadyExists) {
            System.out.printf("topic already exits: %s%n", topicName);
        } else {
            System.out.printf("creating topic: %s%n", topicName);
            NewTopic newTopic = new NewTopic(topicName, numPartitions, (short) 1);
            admin.createTopics(Collections.singleton(newTopic)).all().get();
        }
    }

    /*
    Delete topic
     */
    private static void deleteTopic(String topicName, Properties properties) {
        try (AdminClient client = AdminClient.create(properties)) {
            DeleteTopicsResult deleteTopicsResult = client.deleteTopics(Collections.singleton(topicName));
            while (!deleteTopicsResult.all().isDone()) {
                // Wait for future task to complete
            }
        }
    }
}