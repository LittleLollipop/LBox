package com.littlelollipop.lbox.thread;


import android.os.Handler;
import android.os.Looper;

/**
 * Created by sai on 17/11/29.
 */
public class RunnablePocket {

    static Handler mainHandler = new Handler(Looper.getMainLooper());

    private RunnablePocket() {
    }

    public static boolean post(Runnable runnable) {
        return mainHandler.post(runnable);
    }

    public static void postDelayed(Runnable r, long delayMillis) {
        mainHandler.postDelayed(r, delayMillis);
    }

    public static void removeCallbacks(Runnable runnable) {
        mainHandler.removeCallbacks(runnable);
    }
}
