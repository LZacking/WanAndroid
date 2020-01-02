package com.lzacking.wanandroid.util;

import android.app.Activity;

import java.util.Stack;

public class ActivityManager {

    // 单例模式：饿汉式
    private ActivityManager() {

    }

    private static ActivityManager activityManager = new ActivityManager();

    public static ActivityManager getInstance() {
        return activityManager;
    }

    // 提供栈的对象
    private Stack<Activity> activityStack = new Stack<>();

    // activity的添加
    public void add(Activity activity) {
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    // 删除指定的activity
    public void remove(Activity activity) {
        if (activity != null) {
            // 倒序遍历可以防止空指针异常
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                Activity currentActivity = activityStack.get(i);
                if (currentActivity.getClass().equals(activity.getClass())) {
                    currentActivity.finish(); // 销毁当前的activity（不显示）
                    activityStack.remove(i); // 从栈空间移除
                }
            }
        }
    }

    // 删除当前的activity（即stack栈顶的activity）
    public void removeCurrent() {
        Activity activity = activityStack.lastElement();
        activity.finish();
        activityStack.remove(activity);
    }

    // 删除所有的activity
    public void removeAll() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            activity.finish();
            activityStack.remove(activity);
        }
    }

    // 返回栈大小
    public int size() {
        return activityStack.size();
    }

}
