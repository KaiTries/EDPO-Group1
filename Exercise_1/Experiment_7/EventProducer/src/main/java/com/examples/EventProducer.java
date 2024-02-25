package com.examples;

import com.data.Cat;
import com.data.Dog;
import com.google.common.io.Resources;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class EventProducer {

    public static void main(String[] args) throws Exception {

        ArrayList<String> topics = new ArrayList<>();
        topics.add("cats");
        topics.add("dogs");

        Properties properties;
        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            properties = new Properties();
            properties.load(props);
        }

        KafkaProducer<String, Object> producer = new KafkaProducer<>(properties);

        for(String topic : topics){
            deleteTopic(topic, properties);
            createTopic(topic, 1, properties);
        }

        try {

            int counter = 0;

            while (true) {

                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Object payload = new Object();

                int topicIndex = getRandomNumber(0, topics.size());

                switch (topics.get(topicIndex)){
                    case "cats":
                        payload = new Cat(counter, "Cat-" + counter);
                        break;
                    case "dogs":
                        payload = new Dog(counter, "Dog-" + counter);
                        break;
                    default:
                        System.out.println("Something went wrong...");
                        break;
                }


                producer.send(new ProducerRecord<>(topics.get(topicIndex), payload));

                System.out.println(topics.get(topicIndex) + "-event sent: " + payload);

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