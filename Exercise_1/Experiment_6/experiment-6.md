# Experiment 6: Multiple Brokers

## Goal

* Investigate the behavior of the Kafka cluster when multiple brokers are used

## Setup

In ./docker/docker-compose.yml change the following properties:

broker-1 and broker-2

```
KAFKA_ADVERTISED_HOST_NAME=your_local_ipv4_address
```

Only in broker-1

```
KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://your_local_ipv4_address:9092
```

Only in broker-2

```
KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://your_local_ipv4_address:9093
```

## Steps

1. Start the Kafka cluster
2. Start a Kafka producer
3. Start a Kafka consumer
4. Produce some messages
5. Simulate a broker outage
6. Observe the behavior of the Kafka cluster
7. Recover the broker outage
8. Observe the behavior of the Kafka cluster

## Experiment

1. Start the Kafka cluster

```bash
cd docker
docker-compose up -d
```

2. Start the producer ./producer/src/main/java/com/examples/ClicksProducer.java via IDE
3. Start the consumer ./clickEventConsumer/src/main/java/com/examples/ClickEventConsumer.java via IDE
4. The producer will start producing messages and the consumer will start consuming messages
5. Simulate a Broker outage

```bash
docker-compose stop kafka-broker-1
```

6. Observe the behavior of the Kafka cluster

* Verify if the consumer is still consuming messages -> YES
* Verify if a new consumer can start consuming messages -> YES
  Start the consumer ./gazeEventConsumer/src/main/java/com/examples/gazeEventConsumer.java via IDE
* Verify if a new topic can be created -> YES
* Verify if topics can be listed -> YES

7. Recover the Kafka outage

```bash
docker-compose start kafka-broker-1
```

8. Observe the behavior of the Kafka cluster

* Verify if the consumer is still consuming messages -> YES
* Verify if a new consumer can start consuming messages -> YES
  Start the consumer ./gazeEventConsumer/src/main/java/com/examples/gazeEventConsumer.java via IDE
* Verify if a new topic can be created -> YES
* Verify if topics can be listed -> YES

## Conclusion

When one instance of the kafka service is down, the Kafka cluster is still able to produce and consume messages.
This makes sense since the kafka instances are redundant.
This means that if one instance is down, the other instance can still take over the work of the downed instance.


