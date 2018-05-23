package com.fragmentUtils.fragment;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixuesong on 2018/5/23.
 */
public class ActivityStack {

    private static List<WeakReference<Activity>> listActivities = new ArrayList<WeakReference<Activity>>();

    public final static void addActivity(Activity activity) {
        WeakReference<Activity> weakReference = new WeakReference<Activity>(activity);
        listActivities.add(weakReference);
    }

    public final static void removeActivity(Activity activity) {
        for (WeakReference<Activity> weakReference : listActivities) {
            if (weakReference.get() == activity) {
                listActivities.remove(weakReference);
                break;
            }
        }
    }

    public final static Activity getTopAcitivity() {
        Activity activity = null;
        if (listActivities != null && listActivities.size() > 0) {
            int size = listActivities.size();
            activity = listActivities.get(size - 1).get();
        }
        return activity;
    }


    public final static List<WeakReference<Activity>> getActivityStack() {
        return listActivities;
    }

}
