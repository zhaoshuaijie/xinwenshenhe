package com.lcsd.examines.fengtai.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
public class ActivityManager {
    private static ActivityManager activityManager;
    private static List<Activity> activities = new ArrayList<>();

    private ActivityManager() {

    }

    //单例
    public static ActivityManager getActivityManager() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }
    //添加
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    //删除
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }
    //删除集合内的Activity
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }


}

