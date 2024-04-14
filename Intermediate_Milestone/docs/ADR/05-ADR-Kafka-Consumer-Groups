# 5. Consumer Groups

Updated: 2024-04-14

## Status

Accepted

## Context
Potentially multiple consumers of the same topic may want to consume the messages. In this case,
we need to ensure that each message is consumed by only one consumer in the group.

## Decision
We will use Kafka Consumer Groups to ensure that each message is consumed by only one consumer in the group.
In this case, we will have only one consumer for each station (i.e. microservice).

## Consequences
This enables us to be sure that each message is consumed by only one consumer in the group. Also it allows
multiple services / stations to consume the same topic without any conflicts.