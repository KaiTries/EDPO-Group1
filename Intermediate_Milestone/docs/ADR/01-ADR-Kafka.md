# 1. Kafka

Updated: 2024-04-14

## Status

Accepted

## Context

Our services in the ecosystem need a possibility to exchange information in an organized manner.
We therefore need a way to decouple the services from each other and to provide a way to scale the services
independently.

## Decision

We decided to use Apache Kafka as a message broker for our services. At this stage of development we are
potentially not aware of the full extent of the requirements of the system. Therefore we decided to use
Kafka as the message broker. This lets us decouple the service from each other. We can easily decide that we
want to consume messages from a specific topic if needed.

Furthermore, we have decided to create a topic for each station in the factory. This way we can easily
separate the messages from the different stations.

## Consequences

A local deployment of a Kafka broker is needed (done by docker-compose). This adds complexity to the
development environment. The developers need to be aware of the Kafka broker and how to interact with it.
Also keeping track of the topics and the messages in the topics is needed. This can be tedious and time
intensive.

The individual Kafka topics for each station make it easier to scale the system in the future,
if any station needs to be added or the workflow needs to be extended or changed. Also debugging and
monitoring of the system is easier, as we can easily see which messages are sent to which station
by the topic names.

Also using Kafka as the message broker is a good choice, as Kafka is known for its high throughput which
is potentially needed in our scenario as we are moving more and more towards the topics of stream 
processing in the course. (Keep in mind, Messaging and Stream processing are core specialties of Kafka)
