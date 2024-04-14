# 7. Coordination

Updated: 2024-04-14

## Status

Accepted

## Context
We may choose a coordination mechanism ranging on the spectrum from centralized to decentralized:
* Orchestration (high dynamic coupling)
* Choreography (low dynamic coupling)

## Decision
We use Camunda as a workflow engine to implement the coordination mechanism. Using purely Camunda would
be a centralized approach, thus be orchestration (high dynamic coupling). However, we add event notification
to the mix, using Kafka as the event bus. This way, we can achieve a more decentralized approach.
Therefore, we are using a mix of orchestration and choreography, being more on the orchestration side.

## Consequences
* We have a centralized workflow engine, which can be a single point of failure.
* We have a more decentralized approach by using Kafka as an event bus.
* We have a more complex system, which can be harder to maintain.
* We have a more flexible system, which can be easier to extend.
* If we would only consider the happy path, using orchestration would be the perfect fit, because we
would not need to care about services that might fail. However, we need to consider the unhappy path as well.
* Using Camunda as the workflow engine reduces the risk of creating event chains per accident, which can
happen when using Kafka only.