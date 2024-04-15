# 3. MQTT

Updated: 2024-04-15

## Status

Accepted

## Context
The smart factory is emitting MQTT events. Somehow we need to communicate with the factory to be able to 
react to these events happening in the factory.

## Decision
We will use MQTT to communicate with the factory. To have as few MQTT clients as possible, we will use a 
dedicated MQTT client that will be responsible to consume the MQTT messages from the factory and then 
forward the events to the Kafka broker. 


## Consequences
This decision will allow us to have one single MQTT client that will be responsible  to consume MQTT 
messages. This makes the system easier to maintain, because we have a clear distinction between the 
communication with the factory, that takes place over MQTT, and the communication with the individual 
services in our system, that takes place over Kafka.

Because we have no permanent connection to the actual smart factory, we create a factory simulator that 
simulates the factory and emits MQTT messages for each event that happens in the factory. This is done via 
a data dump, consisting of a list of events that have been recorded during a 1-hour live recording of the 
actual factory. Our simulator then reads those lines and emits a new MQTT event every second.
