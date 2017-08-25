package com.chat.im.helper;

import android.os.Environment;

import java.io.File;

/**
 * SD卡工具类
 */

public class SDCardHelper {

    private static SDCardHelper sdCardHelper;

    private SDCardHelper() {
    }

    public static SDCardHelper getInstance() {
        if (null == sdCardHelper) {
            synchronized (SDCardHelper.class) {
                if (null == sdCardHelper) {
                    sdCardHelper = new SDCardHelper();
                }
            }
        }
        return sdCardHelper;
    }

    public static void clearCache() {
        sdCardHelper = null;
    }

    /**
     * 监测SD卡是否存在
     */
    public boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     */
    public String getSDCardPathWithFileSeparators() {
        return Environment.getExternalStorageDirectory().toString() + File.separator;
    }
}
