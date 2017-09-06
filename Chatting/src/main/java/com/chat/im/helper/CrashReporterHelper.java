package com.chat.im.helper;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.chat.im.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

/**
 * 程序崩溃处理Crash,单例模式
 */

public class CrashReporterHelper {

    private static Thread.UncaughtExceptionHandler mAttachHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            try {
                //创建crash文件夹
                String logCrashDir = SDCardHelper.getInstance().getSDCardPathWithFileSeparators() + ContextHelper.getContext().getString(R.string.app_name) + File.separator + "crash";
                File dir = new File(logCrashDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                //crash文件名称
                String fileName = logCrashDir + File.separator + UtilsHelper.getInstance().formatTime(System.currentTimeMillis()) + ".txt";
                File crashFile = new File(fileName);
                if (!crashFile.exists()) {
                    try {
                        crashFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //将崩溃日志写入文件中
                PackageManager packageManager = ContextHelper.getContext().getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(ContextHelper.getContext().getPackageName(), PackageManager.GET_ACTIVITIES);
                PrintWriter ps = new PrintWriter(crashFile);
                //获取程序版本号
                if (packageInfo != null) {
                    String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                    String versionCode = packageInfo.versionCode + "";

                    ps.append("versionName:").append(versionName);
                    ps.append("\r\n");
                    ps.append("versionCode:").append(versionCode);
                    ps.append("\r\n");
                }
                //获取当前设备信息
                //BRAND:Meizu
                //CPU_ABI:arm64-v8
                //DEVICE:m2note
                //DISPLAY:Flyme 5.1.11.0A
                Field[] fields = Build.class.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        switch (field.getName()) {
                            case "BRAND":
                                ps.append("BRAND:").append(field.get("BRAND").toString());
                                ps.append("\r\n");
                                break;
                            case "DEVICE":
                                ps.append("DEVICE:").append(field.get("DEVICE").toString());
                                ps.append("\r\n");
                                break;
                            case "DISPLAY":
                                ps.append("DISPLAY:").append(field.get("DISPLAY").toString());
                                ps.append("\r\n");
                                break;
                            case "CPU_ABI":
                                ps.append("CPU_ABI:").append(field.get("CPU_ABI").toString());
                                ps.append("\r\n");
                                break;
                        }
                    } catch (Exception e) {
                        ps.append("获取设备信息异常\r\n");
                        e.printStackTrace(ps);
                    }
                }
                ex.printStackTrace(ps);
                ps.flush();
                ps.close();

            } catch (FileNotFoundException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    public static void initCrashReporterInstance() {
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(mAttachHandler);
    }

    public static void clearCache() {
        mAttachHandler = null;
    }
}
