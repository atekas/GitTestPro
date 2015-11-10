package com.sensu.android.zimaogou.utils;

import android.app.Activity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by winter on 2015/9/22.
 *
 * @author winter
 */
public class ActivityManager {

    /**
     * 保存在栈里的所有Activity
     */
    private Set<Activity> mActivities = new HashSet<Activity>();

    /**
     * 当前显示的activity
     */
    private Activity mCurrentActivity = null;

    private Set<Activity> mRegisterLoginActivities = new HashSet<Activity>();

    private static ActivityManager sInstance = new ActivityManager();

    public static ActivityManager getInstance() {
        return sInstance;
    }

    private ActivityManager() {};

    /**
     * 当Activity执行onCreate时调用-保存启动的Activity
     * @param activity 执行onCreate的Activity
     */
    public void onCreate(Activity activity) {
        mActivities.add(activity);
    }

    /**
     * 当Activity执行onDestroy时调用-移除销毁的Activity
     * @param activity 执行onDestroy时的Activity
     */
    public void onDestroy(Activity activity) {
        mActivities.remove(activity);
    }

    /**
     * 关闭所有的activity
     */
    public void finishActivities() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
        mActivities.clear();
    }

    public void addRegisterLoginActivity(Activity activity) {
        mRegisterLoginActivities.add(activity);
    }

    public void finishRegisterLoginActivities() {
        for (Activity activity : mRegisterLoginActivities) {
            activity.finish();
        }
        mRegisterLoginActivities.clear();
    }
}
