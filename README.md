# LBox

## Logic Box

Logic Box is a lightweight implementation package of basic abstract logic
These include:
    State machine
    Method chain
    Manifold valve
    Task trigger

## State Machine

![https://github.com/LittleLollipop/LBox/blob/master/design/StateMachine.png](https://github.com/LittleLollipop/LBox/blob/master/design/StateMachine.png)

The state machine in this library only provides the operating mechanism, and the state is completely defined by the subclass.

To ensure orderly behavior in the state machine, the core logic runs in an independent thread.

The state transition can be triggered by calling the changeState method. The state transition has three steps, check change, state in, and state leave. The three method calls are executed in the internal thread of the state machine in sequence.

  + The check change will pass in the new state of the attempt to migrate and the current state. The developer needs to decide whether the migration can be performed based on the design and other data is complete. If the migration is not possible, the process will end immediately.

  + State leave is called after the check change, where the developer handles the cleanup work needed to leave the current state.

  + State in is called after state leave, where the developer handles the initiation of the new state.


## Method chain

![https://github.com/LittleLollipop/LBox/blob/master/design/Mission.png](https://github.com/LittleLollipop/LBox/blob/master/design/Mission.png)

The method chain is designed to manage a set of tasks or methods in a clear order. By individually defining each step and the method only interacts with the chain, the purpose of separating the top-level business logic is achieved. At the same time, the method-level coupling is also achieved.


## Manifold valve

![https://github.com/LittleLollipop/LBox/blob/master/design/ManifoldValve.png](https://github.com/LittleLollipop/LBox/blob/master/design/ManifoldValve.png)

A simple process merging tool, in parallel processing, usually has a certain function point, which requires multiple data has been obtained or multiple methods have been executed. In this case, the use of the manifold valve can clearly express the logic.


## Task trigger

![https://github.com/LittleLollipop/LBox/blob/master/design/TaskLoader.png](https://github.com/LittleLollipop/LBox/blob/master/design/TaskLoader.png)

Lightweight message queue, for the situation where there are multiple listeners for the same message, different execution levels can be set to process the sequence.
