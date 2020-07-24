# LBox

[中文](https://github.com/LittleLollipop/LBox/blob/master/README_CN.md)

[![](https://www.jitpack.io/v/LittleLollipop/LBox.svg)](https://www.jitpack.io/#LittleLollipop/LBox)

## Logic Box

Logic Box is a lightweight implementation package of basic abstract logic
These include:
  + State machine
  + Method chain
  + Manifold valve
  + Task trigger

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


License
=======

The MIT License (MIT)

Copyright 2020 LittleLollipop

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
