package com.littlelollipop.lbox.tasktrigger;

import android.os.Handler;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by sai on 17/12/1.
 */
public class TaskTrigger {

    private static int taskHash = 10000;
    Handler mHandler = new Handler();

    public static final int TASKLEVEL_0 = 0;
    public static final int TASKLEVEL_1 = 1;
    public static final int TASKLEVEL_2 = 2;
    public static final int TASKLEVEL_3 = 3;
    public static final int TASKLEVEL_4 = 4;
    public static final int TASKLEVEL_5 = 5;

    Map<String, TaskRunner> allTask = new Hashtable<>();

    TaskRunner running;

    public void arrivePoint(String pointName) {

        TaskRunner taskRunner = allTask.get(pointName);

        if (running != null) {
            running.pause();
        }

        if (taskRunner != null) {

            running = taskRunner;
            running.start();

            return;
        } else {
            running = new TaskRunner();
            allTask.put(pointName, running);
        }
    }

    public void registerTask(String point, Task task) {

        TaskRunner taskRunner = allTask.get(point);

        if (taskRunner == null) {
            taskRunner = new TaskRunner();
            allTask.put(point, taskRunner);
        }

        taskRunner.addTask(task);

        //特别鸣谢，需求方:老司机
        if (taskRunner == running) {
            running.start();
        }
    }


    class TaskRunner implements Runnable {

        LinkedList<Task> tasks = new LinkedList<>();

        //        boolean running = false;
        boolean paused = true;

        public void pause() {
            paused = true;
            //            running = false;
        }

        public void start() {

            if (!paused)
                return;

            paused = false;

            mHandler.post(this);

        }

        public void addTask(Task task) {

            if (tasks.size() == 0)
                tasks.add(task);

            for (Task t : tasks) {
                if (t.getLevel() > task.getLevel()) {

                    tasks.add(tasks.indexOf(t), task);

                    return;
                }
            }

            if (!tasks.contains(task))
                tasks.addLast(task);

        }

        @Override
        public void run() {

            paused = false;

            while (tasks.size() > 0) {
                if (paused) {
                    return;
                }

                Task first = tasks.removeFirst();

                first.run();
            }
        }

    }

    public static abstract class Task {

        public abstract int getLevel();

        public abstract void run();

        int hashCode = -1;

        public Task() {
            if (getTaskNumber() == 0) {
                hashCode = ++taskHash;
            } else {
                hashCode = getTaskNumber();
            }
        }

        /**
         * max 9999
         *
         * @return
         */
        public int getTaskNumber() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {

            if (!(o instanceof Task))
                return false;

            return this.hashCode() == o.hashCode();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

}
