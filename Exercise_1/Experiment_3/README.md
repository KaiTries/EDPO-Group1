# Experiment 3: Zookeeper outage

## Goal

* Simulate a Zookeeper outage and observe the behavior of the Kafka cluster

## Setup

* docker/docker-compose.yml Change the property: KAFKA_ADVERTISED_HOST_NAME to your local ipv4 address (ifconfig | grep
  inet)

## Steps

1. Start the Kafka cluster
2. Start a Kafka producer
3. Start a Kafka consumer
4. Produce some messages
5. Simulate a Zookeeper outage
6. Observe the behavior of the Kafka cluster
7. Recover the Zookeeper outage
8. Observe the behavior of the Kafka cluster

## Experiment

1. Start the Kafka cluster

```bash
cd docker
docker-compose up -d
```

2. Start the producer ./producer/src/main/java/com/examples/ClicksProducer.java via IDE
3. Start the consumer ./clickEventConsumer/src/main/java/com/examples/ClicksConsumer.java via IDE
4. The producer will start producing messages and the consumer will start consuming messages
5. Simulate a Zookeeper outage

```bash
docker-compose stop zookeeper
```

6. Observe the behavior of the Kafka cluster

* Verify if the consumer is still consuming messages -> YES
* Verify if a new consumer can start consuming messages -> YES
  Start the consumer ./gazeEventConsumer/src/main/java/com/examples/gazeEventConsumer.java via IDE
* Verify if a new topic can be created -> NO
* Verify if topics can be listed -> NO

7. Recover the Zookeeper outage

```bash
docker-compose start zookeeper
```

8. Observe the behavior of the Kafka cluster

* Verify if the consumer is still consuming messages -> YES
* Verify if a new consumer can start consuming messages -> YES
  Start the consumer ./gazeEventConsumer/src/main/java/com/examples/gazeEventConsumer.java via IDE
* Verify if a new topic can be created -> YES
* Verify if topics can be listed -> YES

## Conclusion

When the zookeeper service is down, the Kafka cluster is still able to produce and consume messages.
However, it is not able to create new topics or list existing topics.
Once the zookeeper service is recovered, the Kafka cluster is able to create new topics and list existing topics.
This makes sense because the zookeeper service is responsible for maintaining the metadata of the Kafka cluster.
