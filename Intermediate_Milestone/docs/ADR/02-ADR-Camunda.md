# 2. Camunda

Updated: 2024-04-15

## Status

Accepted

## Context
For the course of this course, we are asked to use Camunda as the workflow engine.

## Decision
When using Camunda, we had to decide whether we want to use Camunda 7 or Camunda 8.
Therefore, we have decided to Camunda 8, the latest official version. 

## Consequences
By using Camunda, we want to leverage the BPMN 2.0 standard to model our business processes, have a graphical representation of
those processes, and have a way to monitor and manage the execution of those processes. Another
big advantage of using Camunda is, that it is open-source and has a large community, which can help
us with any issues we might encounter.

The decisions of using Camunda 8 brings several advantages. Firstly, it is much easier to use than Camunda 7,
because there exist only one way of how to communicate with the engine, i.e. "external task execution". 
Secondly, it is also much easier to integrate into our system because we don't need to have a local 
Camunda engine running. Instead, we can simply connect to a Camunda Cloud cluster, which is a pretty 
straight-forward process.
Lastly, we think that Camunda 8 will be more stable and have more features than Camunda 7 in the future.