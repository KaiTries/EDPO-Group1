# Experiment 8:  Kafka behaviour with multiple Topics and Partitions
Based on repository [lab02-kafka-producer-consumer](https://github.com/scs-edpo/lab02Part1-kafka-producer-consumer)

## Goal
* Investigate the behaviour of Kafka given multiple Topics and Partitions.

## Experiment Explanation
Realistically an entity using Kafka will use multiple Topics and Partitions.
Therefore, it is important to know how these properties work.


## Setup
* docker/docker-compose.yml Change the property: KAFKA_ADVERTISED_HOST_NAME to your local ipv4 address (ipconfig | grep
  inet)
* ```bash
    kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic topic1_p3
    kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic topic2_p1
    kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 5 --topic topic3_p5  
  ```


## Steps
1. Start the Kafka cluster
    ```bash
    cd docker
    docker-compose up -d
    ```
2. Start Producer (via IDE)
3. Wait a couple seconds to ensure the Producer has sent some messages
3. Start Consumer (via IDE) with the following properties
   ```properties
   bootstrap.servers=localhost:9092
   key.deserializer=org.apache.kafka.common.serialization.StringDeserializer
   value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
   group.id=test
   ```
   * Not setting any property regarding offsets will leave us with the default
5. Observe that the Consumer will not start with message 1 but the first one received after it has started
   * With the default setting of "latest" we experience data loss if the Producer starts sending messages before the Consumer is ready!
   * This happens if the Consumer group that the Consumer is part of has no valid / existing offset to start from.
     * So either a new Consumer Group or an invalid committed offset.
   * If a valid offset has been set, no losses should occur with the property "latest."
6. (Re-)start the Kafka and Producer applications
7. Change to the Consumer source code logic to simulate an error
   ```java
   public class Consumer {
    public static void main(String[] args) throws IOException {
        KafkaConsumer<String, String> consumer;
        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            consumer = new KafkaConsumer<>(properties);
        }

        consumer.subscribe(Arrays.asList("user-events"));
        
        // Counter variable
        int i = 0;

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                // manually commit that we have received the message
                consumer.commitSync();
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                    i++;
                    // simulate a processing error
                    if (i == 50) {
                        throw new IOException("record processing error");
                    }
                }
            }
        } finally {
                consumer.close();
            }
        }
   ```
8. (Re-)start Consumer (via IDE) with the following properties
   ```properties
   bootstrap.servers=localhost:9092
   key.deserializer=org.apache.kafka.common.serialization.StringDeserializer
   value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
   group.id=test
   enable.auto.commit=false
   ```
9. Let the Consumer crash and then restart it again
10. Observe that data loss has occurred!
    * Since we have disabled the automatic commit property, the offset will only get manually updated through our loop
    * But we have committed our offset for the entire received batch before we processed it!
    * This means that if an error occurs and the Consumer needs to restart it will start off at the offset at the end of the batch. So all messages between would be lost.
    * With enable.auto.commit= true, the offset is automatically updated once an entire batch has been successfully processed.
    * If we place the consumer.commitSync(); after the for loop, e.g., after everything is processed we get a similar result!
      * No Data loss occurs as we only commit once successful


## Conclusion
Kafka again proves itself to be a very robust service! Since unless you explicitly try to create data loss, the default 
settings are pretty solid.
The only part that one should check is the auto.offset.reset property.
Since the default of "latest" can lead to unexpected behavior.
If you set it to "earliest" the only risk you run into is record duplication,
which is the preferable problem to have over data loss(in most cases).
Disabling the automatic offset handling by Kafka might enable one to more specifically decide when to commit an offset,
but also makes one more susceptible to possible errors that could result in data loss (as shown above).

