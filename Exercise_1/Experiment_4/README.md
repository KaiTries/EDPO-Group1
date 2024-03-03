# Experiment 4: Risk of data loss due to consumer lag

## Goal
Investigate the risk of data loss in case of high consumer lag.

## Experiment Explanation
Consumer lag is the delay between producer and consumer. Specifically it is the difference
between the offset and the last committed offset within one partition. There are
multiple reasons why consumer lag may occur, such as slow processing logic at the consumer.

For this experiment we will introduce a producer that produces events each 300ms. 
There is also a consumer that takes 700ms to process each event. The consumer
will commit the offset of each event individually. Since the consumer processing rate is slower than
the event creation rate, there should occur long queues of events in the long run.

To force data loss we introduced the following properties to the topic:
 ```
conf.put(TopicConfig.RETENTION_MS_CONFIG, "2000");
conf.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);
 ```

Further down below you will need to specify some more properties to the kafka server.

## Setup
* docker/docker-compose.yml Change the property: KAFKA_ADVERTISED_HOST_NAME to your local ipv4 address (ifconfig | grep
  inet)

## Steps
1. Shut down all processes including Kafka
2. Delete the Docker container in Docker Desktop
3. Start the Kafka cluster
    ```
    cd docker
    docker-compose up -d
    ```
4. Now you need to set the Kafka properties
    * ```
      docker-compose exec kafka /bin/bash
      ```
    * ```
      cd /opt/bitnami/kafka/config
      ```
    * ```
      echo log.retention.check.interval.ms=100 >> server.properties
      echo log.roll.ms=1000 >> server.properties
      ```
    * ```
      cd ../bin
      ```
    * ```
      kafka-server-stop.sh
      ```
    * restart the Kafka cluster in order to make the new properties to take effect.
5. Start ConsumerLagProducer (via IDE)
6. Start ConsumerLagConsumer (viaIDE)
7. Now some data loss events happen about each 20 seconds

## Conclusion
From this experiment the takeaway is that we have to make sure that in no case the built-up queue for
processing events in the consumer gets bigger than the retention allows it. Otherwise, data loss
will be inevitable in the long term.
Keep in mind: Consumer lag results in retransmissions of messages if we restart the consumer.
In general Kafka is very robust against deleting messages which is a good thing.