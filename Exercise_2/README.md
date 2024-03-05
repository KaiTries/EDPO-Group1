# Flowing Retail

This sample application demonstrates a simple order fulfillment system, decomposed into multiple independent components (like _microservices_).

## Lab04 

To run the **Choreography-based** version of the Flowing Retail project for lab04 you first need to be sure that all
the relevant projects have been built at least once:

```
  cd .\kafka\java\
  mvn clean install
```

Then you can execute:

```bash
  docker-compose -f docker-compose-kafka-java-choreography.yml up --build
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
#### Demo
To observe the functionality, simply start the services as described above.
Then place an order by using the endpoint
provided by the [CheckoutService](./kafka/java/checkout/src/main/java/io/flowing/retail/checkout/rest/ShopRestController.java).
It can be accessed by [this link](http://localhost:8091) and clicking the button.
Checking the logs of the notification service, you should see a log message indicating that an email has been sent.




### Event-carried State Transfer
In this part of the exercise, we have extended the [Checkout service](./kafka/java/checkout) such that it is aware of 
the current status of the available goods in the [Inventory](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/domain/Inventory.java).
Therefore, we have adopted the Inventory
in the Checkout service. Additionally we have added a [MessageListener](./kafka/java/checkout/src/main/java/io/flowing/retail/checkout/messages/MessageListener.java)
that is listening on "InventoryStatusEvent"-events. On the other hand, the [Inventory service](./kafka/java/choreography-alternative/inventory) is emitting those
"InventoryStatusEvent"-events each time an [Order](./kafka/java/checkout/src/main/java/io/flowing/retail/checkout/domain/Order.java) gets placed and
[Items](./kafka/java/checkout/src/main/java/io/flowing/retail/checkout/domain/Item.java) get picked from the Inventory.
To make sure the Checkout service obtains the initial Inventory status, the Inventory service emits the initial Inventory status at 
startup of the service using the [MyStartupRunner](./kafka/java/choreography-alternative/inventory/src/main/java/io/flowing/retail/inventory/MyStartupRunner.java).

By doing that we can derive the following implications
* Eventual consistency
* Better fault tolerance because the Inventory service now doesn't have to be running at all time
  * Inventory checking in Checkout service
  * Inventory checking in Inventory service
#### Demo
To observe this scenario, start the services as described above.
The inventory starts with enough items to fulfill the first two orders. 
After that, you can see in the logs of the checkout service,
that it will log a message indicating that the inventory is empty.
Further, the webpage will also show a message indicating that the inventory is empty.
No events will be emitted, since the order will not go through.
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
#### Demo
To test this, some adjustments to the code have to be made, since
our implementation of the Event-carried State Transfer avoids this error scenario
(It could still happen due to eventual consistency, but is not suitable for demo purposes).
+ In [checkoutService/Inventory](./kafka/java/checkout/src/main/java/io/flowing/retail/checkout/domain/Inventory.java)
change the takeItem method, such that it does not check for the amount of items in the inventory.
  ```java
    public Item takeItem(String articleId, int amount) throws NotEnoughGoodsException {
        /*
        for (Item inventoryItem : inventory){
            if (inventoryItem.getArticleId().equals(articleId) && inventoryItem.getAmount() >= amount) {
                inventoryItem.setAmount(inventoryItem.getAmount() - amount);
                Item newItem = new Item();
                newItem.setArticleId(articleId);
                newItem.setAmount(amount);
                return newItem;
            }
        }
        */
        Item newItem = new Item();
        newItem.setArticleId(articleId);
        newItem.setAmount(amount);
        return newItem;
        // throw new NotEnoughGoodsException();
    }
  ```
This is enough to test the error scenario.
Now, after placing two orders, the inventory will be empty.
After that, every order will cause the Inventory to send a "GoodsNotFetchedEvent."
The appropriate services are
listening to this event and logging their theoretical actions.