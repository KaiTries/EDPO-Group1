# Experiment 2: The risk of data loss due to dropped messages
Based on repository [lab02-kafka-producer-consumer](https://github.com/scs-edpo/lab02Part1-kafka-producer-consumer)

Made Producer a bit simpler -> Sends message every second

## Goal
* Investigate the risk of data loss due to dropped messages with different producer configurations.

## Experiment Explanation
Kafka is designed to be a reliable and robust event handler.
If configured correctly no data loss due to dropped messages should occur.
There are however possible configurations where data loss can occur.
Following are experiments to test these scenarios.

The relevant producer properties are:
* acks=xyz (how many acknowledgment responses the producer needs to mark message as successful)
* retries=xyz (how often the producer tries to resend a message upon failure)

### acks
There are three different ack settings: 0,1 and all. 
* 0 - The producer does not wait for a reply before assuming the message was sent successfully.
* 1 - The producer waits until the leader confirms before assuming successfully sent. (Leader confirms as soon as he has received it)
* all - The producer waits until the leader confirms before assuming successfully sent (Leader will only confirm when all in sync replicas received the message)

## Setup
* docker/docker-compose.yml Change the property: KAFKA_ADVERTISED_HOST_NAME to your local ipv4 address (ifconfig | grep
  inet)

## Steps
1. Start the Kafka cluster
    ```bash
    cd docker
    docker-compose up -d
    ```
2. Start Producer (via IDE) with the following properties
    ```
    acks=0
    retries=0
    ```
3. Start Consumer
4. Stop the Container with the Kafka service
5. Wait 5 seconds
6. Start the Container with the Kafka service
7. Observe that both Producer and Consumer will try to reestablish connection
8. Check the console of the Consumer
   * Observe that there are missing messages
   * The missing messages are the ones sent before the Producer realized it lost connection

9. (Re)-start Producer (via IDE) with the following new properties
   ```
   acks=1
   retries=0
   ```
10. Repeat the process of stopping the Container and checking for lost messages
    * We can still observe data loss due to dropped messages 
    * Losses occur less often -> acks=1 adds some security but not enough
    * A bit finicky to replicate / test, might have to kill docker container multiple times to achieve data loss
11. (Re)-start Producer (via IDE) with the following new properties
   ```
   acks=1
   retries=1
   ```
12. Repeat the above process again and check for dropped messages
    * We observe no dropped messages
    * The additional retry is enough to shield us from data loss due to dropped messages in case of broker failure 
    * No need to test acks=all or larger retry number since 1 is already sufficient

## Conclusion
The results of the experiment were unsurprising. Using acks=0 and retries=0 means there are no safeguards in place
regarding data loss due to dropped messages. Therefore, we expected missing messages in the first part. The producer however
notices relatively fast, that the broker has become unavailable and so the timeframe in which messages can be lost is not too large.
Adding acks=1 added some degree of protection but was not sufficient. It was hard to confirm this since the protection meant
that sometimes there would be no data losses for multiple restarts of the docker container. 

Adding retries=1 was then sufficient to stop us from creating data losses through dropped messages by killing the docker container.