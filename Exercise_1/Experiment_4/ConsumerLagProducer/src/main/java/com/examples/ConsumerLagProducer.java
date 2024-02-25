package com.examples;

import com.google.common.io.Resources;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.TopicConfig;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConsumerLagProducer {

    public static void main(String[] args) throws Exception {

        /*
            consumer-lag-events are created each 300ms
         */

        String topic = "consumer-lag-events";

        Properties properties;
        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            properties = new Properties();
            properties.load(props);
        }

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        deleteTopic(topic, properties);
        createTopic(topic, 1, properties);

        try {

            int counter = 0;

            while (true) {

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                producer.send(new ProducerRecord<>(topic, String.valueOf(counter)));

                System.out.println("consumer-lag-event sent: " + counter);

                counter++;

            }

        } catch (Throwable throwable) {
            System.out.println(throwable.getStackTrace());
        } finally {
            producer.close();
        }
    }

    /*
    Generate a random number
    */
    private static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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

            /*
                See here the custom retention and cleanup policy settings
             */
            Map<String, String> conf = new HashMap<>();
            conf.put(TopicConfig.RETENTION_MS_CONFIG, "2000");
            conf.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);
            NewTopic newTopic = new NewTopic(topicName, numPartitions, (short) 1)
                    .configs(conf);
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