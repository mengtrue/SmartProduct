package com.smart.httpdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.smart.httpdemo.tool.OkhttpConfiguration;
import com.smart.httpdemo.tool.OkhttpUtils;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();
    /**
     * the running activity number
     * if the count is zero, the APP not run
     */
    private static int mActivityCount = 0;

    private LogUtils.Config mLogConfig = null;

    @Override
    public void onCreate() {
        super.onCreate();
        OkhttpConfiguration.Builder builder = new OkhttpConfiguration.Builder();
        OkhttpUtils.getInstance().init(builder.build());
        Utils.init(this);
        this.registerActivityLifecycleCallbacks(new SmartActivityLifeCycleCallback());
        mLogConfig = LogUtils.getConfig();
        LogUtils.dTag(TAG, mLogConfig);
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

    public int getRunningActivityCount() {
        return mActivityCount;
    }

    private class SmartActivityLifeCycleCallback implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            mActivityCount++;
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivityCount--;
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
