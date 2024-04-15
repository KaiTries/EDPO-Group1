# 4. Services

Updated: 2024-04-15

## Status

Accepted

## Context
For the development of our system, it is essential to get a clear mental model of which services we want 
to implement. This will help us to understand the domain better and make more suitable decisions in the 
future.

## Decision
While brainstorming about the process workflow that we aim to model, we have decided to create the 
following services with the corresponding functionalities:
* FactorySimulator: This service will be responsible for simulating the factory's behavior.
* FactoryListener: This service will be responsible for listening to the factory's events and forwarding 
  them to the respective services via Kafka.
* Order: This service will be responsible for managing the orders.
* Warehouse: This service will be responsible for managing the High-bay Warehouse (HBW).
* Gripper: This service will be responsible for managing the Vacuum Gripper (VGR).
* Delivery: This service will be responsible for managing the Delivery & Pickup station (DPO).
* Monitoring: This service will be responsible for monitoring the factory's behavior (So far, Order and 
  Warehouse).


## Consequences
Because we decided to create a service for each station in the factory, we will have easy to understand, 
and very clear responsibilities and decision boundaries between the individual services. This will make
it easier to maintain and extend the system in the future, if we decide to add some more functionality to 
the modelled process workflow.

Having this mental model of the system layout, implementing the system will get much more straightforward.