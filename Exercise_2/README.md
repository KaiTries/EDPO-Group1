# Flowing Retail

This sample application demonstrates a simple order fulfillment system, decomposed into multiple independent components (like _microservices_).

## Lab04 

To run the **Choreography-based** version of the Flowing Retail project for lab04 you first need to be sure that all
the relevant projects have been built at least once:

```bash
  $ cd .\kafka\java\
  $ mvn clean install
```

Then you can execute:

```
  $ docker-compose -f docker-compose-kafka-java-choreography.yml up --build
```
from the directory [runner/docker-compose](runner/docker-compose).

## Implementation of Exercise 2 - Part 2
### Email Notification
In this part of the exercise,
we have added a new Service [NotificationService](./kafka/java/choreography-alternative/notification).
The Service listens to all events relevant to the order process and sends an appropriate email to the customer.
It maps the messages of the events to appropriate java objects, so that it can use the information to email the customer.
Currently, the sending of an email is just emulated by a log message,
since implementing a real email sender is out of the scope of the exercise and does not help with understanding kafka.
See following classes for implementation details:


### Event State Transfer
### Error Scenario
In this part of the exercise, we have extended the [Inventory](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/domain/Inventory.java) such that it can handle orders that require a greater amount
of one or more items than the [Inventory](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/domain/Inventory.java) has. In this case, the Inventory will raise an [NotEnoughGoodsException](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/domain/NotEnoughGoodsException.java) that will be catched
and handled by the [InventoryService](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/application/InventoryService.java) that will propagate the error to the [MessageListener](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/messages/MessageListener.java) that will then send a new event "GoodsNotFetchedEvent"
which indicates that the goods have not been picked up. See following classes for implementation details:

- [Inventory](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/domain/Inventory.java)
- [InventoryService](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/application/InventoryService.java)
- [NotEnoughGoodsException](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/domain/NotEnoughGoodsException.java)
- [PickOrderNotFullfilledException](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/domain/PickOrderNotFulfilledException.java)
- [MessageListener](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/messages/MessageListener.java)