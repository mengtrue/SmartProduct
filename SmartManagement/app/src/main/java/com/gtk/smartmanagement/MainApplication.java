package com.gtk.smartmanagement;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.gtk.smartmanagement.activity.LoginActivity;

import java.util.LinkedList;
import java.util.List;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();
    /**
     * the running activity number
     * if the count is zero, the APP not run
     */
    private static int mActivityCount = 0;
    private List<Activity> activityList = new LinkedList<>();

    private LogUtils.Config mLogConfig = null;

    @Override
    public void onCreate() {
        super.onCreate();

        this.registerActivityLifecycleCallbacks(new SmartActivityLifeCycleCallback());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        Utils.init(this);
        mLogConfig = LogUtils.getConfig();
        mLogConfig.setBorderSwitch(false);
        mLogConfig.setLogHeadSwitch(false);
        mLogConfig.setSingleTagSwitch(false);
        //LogUtils.dTag(TAG, mLogConfig);
    }

    /**
     * get resource string from resId
     * @param resId
     * @return the resource string
     */
    public String getResString(@StringRes int resId) {
        Context context = getApplicationContext();
        return (context == null) ? null : context.getResources().getString(resId);
    }

    public void exitLogin() {
        for (Activity activity : activityList) {
            activity.finish();
        }

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class SmartActivityLifeCycleCallback implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            mActivityCount++;
            activityList.add(activity);
            LogUtils.dTag(TAG, "onCreated activity = " + activity.getLocalClassName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivityCount--;
            activityList.remove(activity);
            LogUtils.dTag(TAG, "onDestroy activity = " + activity.getLocalClassName());
        }

        @Override
        public void onActivityResumed(Activity activity) {}

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

        @Override
        public void onActivityStopped(Activity activity) {}

        @Override
        public void onActivityStarted(Activity activity) {}

        @Override
        public void onActivityPaused(Activity activity) {}
    }
}
