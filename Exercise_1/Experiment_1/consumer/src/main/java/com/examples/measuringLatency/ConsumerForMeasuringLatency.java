package com.examples.measuringLatency;

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

public class ConsumerForMeasuringLatency {

    public static void main(String[] args) throws IOException, ParseException {

        KafkaConsumer<String, Object> consumer;
        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            consumer = new KafkaConsumer<>(properties);
        }

        /*
            latency-events are produced at a rate of 200Hz
            They produce a payload with a timestamp. Therefore, upon receiving a latency-event
            the consumer can calculate the difference between the creation and the processing of the event.
         */

        consumer.subscribe(Arrays.asList("latency-events"));

        while (true) {

            ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(5));

            for (ConsumerRecord<String, Object> record : records) {
                switch (record.topic()) {
                    case "latency-events":
                        String value =   record.value().toString();
                        long eventCreatedTime = Long.parseLong(((LinkedHashMap) record.value()).get("timestamp").toString());
                        long eventProcessedTime = System.nanoTime();
                        long elapsedTime = eventProcessedTime - eventCreatedTime;
                        System.out.println("elapsedTime (in millis): " + elapsedTime / 1_000_000);
                        break;
                    default:
                        throw new IllegalStateException("Shouldn't be possible to get message on topic " + record.topic());
                }
            }
        }
    }
}