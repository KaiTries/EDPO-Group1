# Experiment 8:  Kafka behaviour with multiple Topics and Partitions
Based on repository [lab02-kafka-producer-consumer](https://github.com/scs-edpo/lab02Part1-kafka-producer-consumer)

## Goal
* Investigate the behaviour of Kafka given multiple Topics and Partitions.

## Experiment Explanation
Realistically an entity using Kafka will use multiple Topics and Partitions.
Therefore, it is important to know how these properties work.
Following a few scenarios that illustrate the behaviour.


## Setup
* docker/docker-compose.yml Change the property: KAFKA_ADVERTISED_HOST_NAME to your local ipv4 address (ipconfig | grep
  inet)

## Steps
1. Start the Kafka cluster
    ```bash
    cd docker
    docker-compose up -d
    ```
2. Navigate into the Terminal of the Docker Container running Kafka and execute the following commands
    ```bash
        kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic topic1
        kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic topic2
        kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 5 --topic topic3
    ```
    * We have now created 3 different topics with 3,1 and 5 partitions each.
3. Start Producer (via IDE)
4. Start the Consumer (via IDE)
5. Observe that all three topics are balanced
    * If the Producer has already published messages and the Consumer gets a batch, instantly the messages are ordered by topic name, so it will first receive all stored up messages from topic1, topic2 then topic3
    * Once caught up, the Consumer efficiently processes the messages concurrently, achieving load balancing.
    * No topic is left behind / disregarded.
6. Observe partition assignment is random
    * Once a Consumer joins he gets assigned a partition (if there is multiple)
    * He will receive records only from the assigned partition
7. Restart the Consumer (via IDE)
    * Once the Consumer has restarted, we will see that he has been assigned a new partition
    * We can also observe an overlap, since we have auto.offset.reset=earliest. 
    * In the beginning the Consumer receives the last messages from the old partition 
    * After that the new messages are only received through the new partition
    * We can also observe in the logs how the Consumer sets the offsets for each partition
      ````
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic3-3 to the committed offset FetchPosition{offset=179, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic1-1 to the committed offset FetchPosition{offset=341, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic3-2 to the committed offset FetchPosition{offset=537, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic1-0 to the committed offset FetchPosition{offset=67, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic3-4 to the committed offset FetchPosition{offset=246, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic1-2 to the committed offset FetchPosition{offset=1298, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic3-1 to the committed offset FetchPosition{offset=403, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic2-0 to the committed offset FetchPosition{offset=1706, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      [main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-test-1, groupId=test] Setting offset for partition topic3-0 to the committed offset FetchPosition{offset=341, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1001 rack: null)], epoch=0}}
      ````
8. Producer with set Partition / key
   * We can run the same experiment but have the producer specify the partition and key when he sends a message
   ````java
      producer.send(new ProducerRecord<String, String>(
          "topic1", // topic
          1, // partition
          "key" // key
          "some_value_" + System.nanoTime())); // value
   ````
   * In this case even if we restart our Consumer we will still receive messages from the same partition 
9. Starting second Consumer
   * We see that the second Consumer also automatically gets assigned to free partitions
   * In this case he does not receive any messages, because everything can be handled over 1 partition
10. Stop the first Consumer
   * We see that one Kafka notices that the first Consumer is down, the second Consumer gets assigned the partitions of the first Consumer. 
   * Everything is happens automatically
11. Restart the first Consumer
   * Immediately after restarting the first Consumer, the partitions are again allocated and it is as nothing has happened!


## Conclusion
Kafka again proves itself to be a very robust service!
It can evenly distribute messages across partitions within each topic, if necessary.
The consumer can efficiently processs messages from multiple topics and partitions concurrently.
If everyhing is correctly configured Kafka should handle workload efficiently ensuring fault tolerance
and preventing message loss.
