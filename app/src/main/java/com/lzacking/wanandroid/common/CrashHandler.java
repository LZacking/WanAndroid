package com.lzacking.wanandroid.common;

import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.lzacking.wanandroid.util.ActivityManager;
import com.lzacking.wanandroid.util.UIUtils;

import androidx.annotation.NonNull;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    // 系统默认的处理未捕获异常的处理器
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    // 单例模式：（懒汉式）
    private CrashHandler() {

    }

    private static CrashHandler crashHandler = null;

    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public void init() {
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    // 一旦系统出现未捕获的异常，就会调用如下的回调方法
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(UIUtils.getContext(), "出现了未捕获的异常了！", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        // 收集异常信息
        collectionException(e);
        try {
            Thread.sleep(2000);
            // 移除当前activity
            ActivityManager.getInstance().removeCurrent();
            // 结束当前的进程
            android.os.Process.killProcess(android.os.Process.myPid());
            // 结束虚拟机
            System.exit(0);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void collectionException(Throwable ex) {
        final String exMessage = ex.getMessage();
        // 收集具体的客户的手机，系统的信息
        final String message = Build.DEVICE + ":" + Build.MODEL + ":" + Build.PRODUCT + ":" + Build.VERSION.SDK_INT;
        // 发送给后台此异常信息
        new Thread() {
            @Override
            public void run() {
                // 需要按照指定的URL，访问后台的sevlet，将异常信息发送出去
                Log.e("TAG", "exception = " + exMessage);
                Log.e("TAG", "message = " + message);
            }
        }.start();
    }

}
