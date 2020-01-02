package com.lzacking.wanandroid.common;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MyApplication extends Application {

    // 在整个应用执行过程中，需要提供的变量
    public static Context context; // 需要使用的上下文对象
    public static Handler handler; // 需要使用的Handler
    public static Thread mainThread; // 提供主线程对象
    public static int mainThreadId; // 提供主线程对象的id

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread(); // 实例化当前Application的线程即为主线程
        mainThreadId = android.os.Process.myPid();
    }

}
