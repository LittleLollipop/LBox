# LBox

[English](https://github.com/LittleLollipop/LBox/blob/master/README.md)

[![](https://www.jitpack.io/v/LittleLollipop/LBox.svg)](https://www.jitpack.io/#LittleLollipop/LBox)

## Logic Box

Logic Box 是一个轻量级的基础抽象逻辑实现包
其中包括：
  + 状态机
  + 方法链
  + 歧管阀
  + 任务触发器

## State Machine

![https://github.com/LittleLollipop/LBox/blob/master/design/StateMachine.png](https://github.com/LittleLollipop/LBox/blob/master/design/StateMachine.png)

此库当中的状态机仅提供运行机制，状态完全由子类制定。

为保证状态机内行为有序，核心逻辑运行于独立线程中。

调用changeState方法即可触发状态迁移，状态迁移有三个步骤，check change，state in，state leave，三个方法调用依次执行于状态机内部线程中。

  + check change 会传入试图进行迁移的新状态以及当前所处状态，需要开发者根据设计以及其他数据是否齐全决定是否可以进行迁移，如果不可迁移，流程会立即结束。

  + state leave 在check change之后被调用，开发者在这里处理当前状态离开所需要进行的清理工作。

  + state in 在 state leave 之后被调用，开发者在这里处理新状态的启动。


## Method chain

![https://github.com/LittleLollipop/LBox/blob/master/design/Mission.png](https://github.com/LittleLollipop/LBox/blob/master/design/Mission.png)

方法链被设计用来管理有明确顺序的一组任务或方法，通过单独定义每个步骤以及方法仅与链交互，达到分离顶层业务逻辑的目的。同时也做到了方法层面的接耦。


## Manifold valve

![https://github.com/LittleLollipop/LBox/blob/master/design/ManifoldValve.png](https://github.com/LittleLollipop/LBox/blob/master/design/ManifoldValve.png)

一个简单的流程合并工具，在并行处理时，通常会有某一个功能点，需要多个数据已经获取或者多个方法已经执行。这种情况下使用歧管阀可以清晰的表达逻辑。


## Task trigger

![https://github.com/LittleLollipop/LBox/blob/master/design/TaskLoader.png](https://github.com/LittleLollipop/LBox/blob/master/design/TaskLoader.png)

轻量级的消息队列，针对同一条消息有多个监听者的情况，可以设置不同的执行等级来处理先后次序。

## Import

    allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}


    dependencies {
        implementation 'com.github.LittleLollipop:LBox:0.1.5'
    }
    
License
=======

The MIT License (MIT)

Copyright 2020 LittleLollipop

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
