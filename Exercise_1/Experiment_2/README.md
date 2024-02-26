# Experiment 2 - the risk of data loss due to dropped messages
Based on repository [lab02-kafka-producer-consumer](https://github.com/scs-edpo/lab02Part1-kafka-producer-consumer)
Made Producer a bit simpler -> Sends message every second

## Experiment setup - Dropping messages
### Simplest scenario - Broker down & acks=0, retries=0
We can see that if we run this setup without any safeguards in place (acks and retries at 0) we can observe dataloss.

#### Steps
- check that producer.properties has acks=0, retries=0
- run docker-compose up to start the kafka service
- start the Producer -> sends an event every second
- start the Consumer -> prints out the event
- Simply stop the container with the Kafka service, wait a couple seconds and then restart
- observe that both Producer and Consumer will try to reestablish connection with Kafka service
- observe that Consumer will have skipped some messages and data loss has happened

#### takeaways
- Not surprising that data loss occurs, since no safety measures in place
- Not too relevant since in practice no one runs with acks=0, retries=0 and exactly 1 broker

### Same Scenario - Broker down & acks=1, retries 0
- check that producer.properties has acks=1, retries=0
Even with acks=1, we can observe data loss due to dropped messages.
Stopping the docker-kafka Container and then restarting the container leads to the Consumer missing messages.
 - It is a bit of an unreliable way to test, since sometimes no messages are lost.
   - This is most likely due to the sending in batches

### Same Scenario - Broker down & acks=1, retries 1
- check that producer.properties has acks=1, retries=0
Was not able to produce any data lass due to dropped messages with acks=1, retries=1! The additional retry on a 
message already seems good enough to protect against sudden downtime of the broker.


