# Experiment 7: Consumers and Consumer Groups

## Goal
Investigate the behavior of Kafka with multiple Consumers and Consumer Groups.

## Experiment Explanation
In this simple experiment we will explore how Kafka behaves when there are multiple Consumers divided into multiple Consumer Groups.
The following elements are in place:
* 2 topics
  * "cats"
  * "dogs"
* 1 producer
* 2 consumer (grp1, grp2) groups, each having...
  * 1 consumer for "cats"
  * 2 consumer for "dogs"

## Setup
* docker/docker-compose.yml Change the property: KAFKA_ADVERTISED_HOST_NAME to your local ipv4 address (ifconfig | grep
  inet)

## Steps
1. Start the Kafka cluster
    ```
    cd docker
    docker-compose up -d
    ```
2. Start EventProducer (via IDE)
3. Start EventConsumer1 (via IDE): grp1
   * start ConsumerCat
   * start ConsumerDog1
   * start ConsumerDog2
4. Start EventConsumer2 (via IDE): grp2
   * start ConsumerCat
   * start ConsumerDog1
   * start ConsumerDog2
5. Verify that the for all consumer groups the "cats" consumers receive all "cats" messages
6. Verify that only one "dogs" consumer per consumer group receives all "dogs" messages
7. Stop both ConsumerDog2 consumers
8. Verify that after some seconds both ConsumerDog1 consumers take over

## Conclusion
Consumers and Consumer Groups behave like expected:
* 1 consumer per consumer group and topic receives all messages
* the rest is idle and serves as backup consumer