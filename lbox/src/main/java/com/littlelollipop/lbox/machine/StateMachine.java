package com.littlelollipop.lbox.machine;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.littlelollipop.lbox.thread.RunnablePocket;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sai on 17/11/29.
 */
public abstract class StateMachine {

    private final String TAG = StateMachine.this.getClass().getName();

    private static ConcurrentHashMap<String, HandlerThreadHolder> threads = new ConcurrentHashMap<>();
    State            stateNow;
    ArrayList<State> allState;

    protected Handler mHandler;
    private   Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    boolean stopped   = false;
    boolean starting  = false;
    boolean autoStart = false;

    State firstState;

    /**
     * @param allState   All states in this state machine
     * @param firstState Initial state
     * @param autoStart  Automatically start.
     *                   If this function is enabled,
     *                      the state machine will enter the first state after the state machine is fully started,
     *                      and the related callback will be received.
     *                      The state will be empty before the complete start.
     *                   Turning off this function will default to the first state, there is no empty state,
     *                   and there will be no callback to enter the first state.
     */
    protected void init(ArrayList<State> allState, State firstState, boolean autoStart) {
        this.autoStart = autoStart;
        this.firstState = firstState;

        if (autoStart) {
            doInit(allState, null);
        } else {
            doInit(allState, firstState);
        }
    }

    Runnable init = new Runnable() {
        @Override
        public void run() {

            if (mHandler == null) {

                HandlerThreadHolder threadHolder = threads.get(TAG);

                if (threadHolder == null) {
                    threadHolder = new HandlerThreadHolder(TAG);
                    threads.put(TAG, threadHolder);
                }
                mHandler = threadHolder.buildHandler();

            }
            starting = false;

            if (autoStart) {
                changeState(firstState);
            }
        }
    };

    Runnable close = new Runnable() {
        @Override
        public void run() {

            if (starting) {
                RunnablePocket.postDelayed(this, 10);
                return;
            }

            HandlerThreadHolder threadHolder = threads.get(TAG);

            if (threadHolder != null) {
                stopped = true;
                threadHolder.destroyHandler(mHandler);
                mHandler = null;

                if (threadHolder.clientNumber == 0) {
                    threads.remove(TAG);
                } else if (threadHolder.clientNumber < 0) {
                    threads.remove(TAG);
                    Log.e(TAG, "you need To burn incense and pray");
                }

            } else {
                if (stopped) {
                    Log.e(TAG, "call stop to much ");
                } else {
                    Log.e(TAG, "call stop() before init() or call stop() more than init() ");
                }
            }
        }
    };

    protected final void doInit(final ArrayList<State> allState, final State firstState) {

        starting = true;
        StateMachine.this.allState = allState;
        StateMachine.this.stateNow = firstState;


        /**
         * If init and stop are called at the same time in multiple threads,
         * a new state may be triggered.
         * A thread that has been stopped or is being used is used,
         * resulting in random null pointers in the underlying Looper and MessageQueue.
         */
        if (Looper.myLooper() != Looper.getMainLooper()) {
            RunnablePocket.post(init);
        } else {
            init.run();
        }

    }

    public final void stop() {

        /**
         * If init and stop are called at the same time in multiple threads,
         * a new state may be triggered.
         * A thread that has been stopped or is being used is used,
         * resulting in random null pointers in the underlying Looper and MessageQueue.
         */
        if (Looper.myLooper() != Looper.getMainLooper()) {
            RunnablePocket.post(close);
        } else {
            close.run();
        }


    }

    public void changeState(final String stateName) {

        if (stopped) {
            Log.d(TAG, "state machine is stopped");
            return;
        }

        if (mHandler == null) {
            RunnablePocket.postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeState(stateName);
                }
            }, 10);
        } else {
            for (State state : allState) {
                if (state.stateName.equals(stateName)) {
                    mHandler.post(new ChangeTask(state));
                }
            }
        }
    }

    public void runInMachine(Runnable runnable) {
        if (mHandler == null) {
            RunnablePocket.postDelayed(runnable, 10);
        } else {
            mHandler.post(runnable);
        }
    }

    public void changeState(final State newState) {

        if (stopped) {
            Log.d(TAG, "state machine is stopped");
            return;
        }

        if (mHandler == null || (autoStart && stateNow == null && newState != firstState)) {
            RunnablePocket.postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeState(newState);
                }
            }, 10);
        } else {
            for (State state : allState) {
                if (state.stateName.equals(newState.stateName)) {
                    mHandler.post(new ChangeTask(state));
                }
            }
        }

    }

    public State getStateNow() {
        return stateNow;
    }

    public abstract boolean checkChange_InMachineThread(State newState, State oldState);

    public abstract void stateIn_InMachineThread(State state, Handler mainThreadHandler);

    public abstract void stateLeave_InMachineThread(State state, Handler mainThreadHandler);

    public void refresh() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                stateIn_InMachineThread(stateNow, mainThreadHandler);
            }
        });
    }

    class ChangeTask implements Runnable {

        State newState;

        ChangeTask(State newState) {
            this.newState = newState;
        }

        @Override
        public void run() {
            doChange(newState);
        }

        private void doChange(State newState) {

            if (stateNow == null && autoStart) {
                stateNow = newState;
                stateIn_InMachineThread(stateNow, mainThreadHandler);
                return;
            }

            if (checkChange_InMachineThread(newState, stateNow) && newState != stateNow) {
                stateLeave_InMachineThread(stateNow, mainThreadHandler);
                stateNow = newState;
                stateIn_InMachineThread(stateNow, mainThreadHandler);
                return;
            }
        }
    }

    public static class State {

        public int    stateNumber;
        public String stateName;

        public State(int stateNumber, String stateName) {
            this.stateName = stateName;
            this.stateNumber = stateNumber;
        }

    }

    public static class HandlerThreadHolder {

        private static final String TAG = HandlerThreadHolder.class.getName();
        HandlerThread mThread;
        String        name;

        int clientNumber = 0;
        public ArrayList<Handler> handlers = new ArrayList<>();

        public HandlerThreadHolder(String name) {
            this.name = name;
            mThread = new HandlerThread(name);
            mThread.start();
        }

        public Handler buildHandler() {
            clientNumber++;

            if (mThread.getLooper() == null) {
                reBuildThread();
            }

            Handler handler = new Handler(mThread.getLooper());
            handlers.add(handler);
            return handler;
        }

        private void reBuildThread() {
            mThread = new HandlerThread(name);
            mThread.start();

            if (handlers.size() > 0) {
                Log.e(TAG, "The thread is forced to stop :" + handlers.size());
            }

            handlers.clear();
        }

        public void destroyHandler(Handler mHandler) {

            if (handlers.remove(mHandler)) {
                mHandler.removeCallbacksAndMessages(null);
                clientNumber--;

                if (clientNumber <= 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        mThread.quitSafely();
                    } else {
                        mThread.quit();
                    }
                }
            } else {
                Log.e(TAG, "call stop to much " + name);
            }

        }
    }

}

