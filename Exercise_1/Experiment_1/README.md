# Experiment 1: Impact of Load and Batch size on Latency

## Goal
Investigate the impact of changing the load and batch size on the processing latency.

## Experiment Explanation
Kafka is designed to handle high-throughput, low-latency data processing.
Certain configurations and workload patterns can influence latency.
This introduces a trade-off between throughput and latency.

To fine-tune the trade-off, Kafka provides two properties for the producer:
* linger.ms=xyz (the time Kafka is willing to wait before sending a batch, even if the batch is not full)
* batch.size=xyz (the maximum size of a batch)


:heavy_plus_sign: larger batch sizes improve throughput as more messages are sent in a single request.


:heavy_minus_sign: larger batch sizes increase latency as messages wait in the producer's buffer until a batch is filled.


## Setup
* docker/docker-compose.yml Change the property: KAFKA_ADVERTISED_HOST_NAME to your local ipv4 address (ifconfig | grep
  inet)

## Steps
1. Start the Kafka cluster
    ```
    cd docker
    docker-compose up -d
    ```
2. Start LatencyEventProducer (via IDE) with the following properties
    ```
    linger.ms=10000
    batch.size=16000
    ```
3. Start ConsumerForMeasuringLatency
4. Observe how the latency behaves
    * By setting linger.ms=10000, Kafka fills up every batch
    * The latency of the first message in each batch is about 1800ms
    * A batch arrives about each 1.8 seconds
    * Decreasing linger.ms below 1.8 seconds would result in Kafka not filling up each batch
5. (Re)-start LatencyEventProducer (via IDE) with the following new properties
    ```
    linger.ms=10000
    batch.size=64000
    ```
6. Observe how the latency behaves
    * By setting batch.size=64000, Kafka still fills up every batch
    * The latency of the first message in each batch is about 7300ms
    * A batch arrives about each 7.3 seconds
    * Increasing batch size means that more data gets compressed into one batch, thus less requests are needed,
   but this may introduce a higher latency.

## Conclusion
Kafka itself is very clever when it comes to managing its batch size. Increasing only the batch.size did not
increase the processing latency because there is too few data that could fill the batch.size before it gets published
right away. To simulate a longer processing latency it was necessary to adjust the linger.ms value. This makes Kafka a quite
robust framework.