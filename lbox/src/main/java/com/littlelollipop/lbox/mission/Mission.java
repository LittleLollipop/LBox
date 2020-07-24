package com.littlelollipop.lbox.mission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sai on 17/12/1.
 */
public class Mission {
    Map<String, Task> tasks = new HashMap<>();

    protected void registerTask(List<StepDisposer> task, String taskName) {
        tasks.put(taskName, new Task(task));
    }

    public void restartTask(String taskName) {
        Task task = tasks.get(taskName);
        if (task != null)
            task.stopped = false;
    }

    protected void stopTask(String taskName) {
        Task task = tasks.get(taskName);
        if (task != null)
            task.stopped = true;
    }

    public void startTask(String taskName, String stepName, Object[] tag) {
        Task task = tasks.get(taskName);

        if (task == null) {
            throw new RuntimeException("not found task");
        }

        task.start(taskName, stepName, tag);
    }

    protected class Task {

        boolean stopped = false;

        List<StepDisposer> stepDisposers;

        String stepNow = "";

        String taskName;

        Task(List<StepDisposer> stepDisposers) {
            this.stepDisposers = stepDisposers;
        }

        public void start(String taskName, String stepName, Object[] tag) {
            this.taskName = taskName;
            stepNow = stepName;
            goNext(tag);
        }

        public void goNext(Object[] tag) {

            if (stopped)
                return;

            for (StepDisposer sd : stepDisposers) {

                if (sd.getStepName().equals(stepNow)) {

                    sd.go(taskName, stepNow, this, tag);
                    return;
                }
            }
        }

        public void changeStep(String step) {
            this.stepNow = step;
        }

        public void doNext(String step, Object[] tag) {

            if (stepNow.equals(step))
                return;

            changeStep(step);
            goNext(tag);
        }
    }

    protected abstract static class StepDisposer {

        protected abstract String getStepName();

        protected abstract void dispose(String taskName, String stepName, Task task, Object[] tag);

        void go(String taskName, String stepName, Task task, Object[] tag) {
            dispose(taskName, stepName, task, tag);
        }

    }
}

