package com.example.g_jiaoyan.qq_hx.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
//线程封装工具类
public class ThreadUtils  {
    //线程池  newSingleThreadExecutor单线程线程池
    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    //子线程中使用的
    public static void runOnSubThread(Runnable runnable){
        sExecutor.execute(runnable);
    }

    //主线程中使用的
    public static void runOnUIThread(Runnable runnable){
        sHandler.post(runnable);
    }
}
