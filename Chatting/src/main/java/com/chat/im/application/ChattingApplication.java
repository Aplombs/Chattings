package com.chat.im.application;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.CrashReporterHelper;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.MD5Helper;
import com.chat.im.helper.OKHttpClientHelper;
import com.chat.im.helper.RequestURLHelper;
import com.chat.im.helper.SDCardHelper;
import com.chat.im.helper.SpHelper;
import com.chat.im.helper.UIHelper;
import com.chat.im.helper.UtilsHelper;

import io.rong.imlib.RongIMClient;


public class ChattingApplication extends MultiDexApplication {

    private Context applicationContext;

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();

        ContextHelper.init(applicationContext);

        initRongCloud();

        //处理程序崩溃日志
        CrashReporterHelper.initCrashReporterInstance();
    }

    //初始化融云
    private void initRongCloud() {
        /*
         * OnCreate()会被多个进程重入,这段保护代码,确保只有您需要使用RongIMClient的进程和Push进程执行了init()
         * io.rong.push:为融云push进程名称,不可修改
         */
        String processName = getCurProcessName(applicationContext);
        if (getApplicationInfo().packageName.equals(processName) || "io.rong.push".equals(processName)) {
            RongIMClient.init(this);
        }
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        LogHelper.e("程序终止的时候执行=====onTerminate()====");
        clearCache();
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        LogHelper.e("低内存的时候执行=====onLowMemory()====");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        LogHelper.e("程序在内存清理的时候执行=====onTrimMemory()====");
        super.onTrimMemory(level);
    }

    private void clearCache() {
        SpHelper.clearCache();
        OKHttpClientHelper.clearCache();
        RequestURLHelper.clearCache();
        UtilsHelper.clearCache();
        UIHelper.clearCache();
        CrashReporterHelper.clearCache();
        MD5Helper.clearCache();
        RequestURLHelper.clearCache();
        SDCardHelper.clearCache();
        ContextHelper.clearCache();
    }
}
