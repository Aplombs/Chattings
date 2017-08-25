package com.chat.im.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.chat.im.R;

import java.util.Map;

import io.reactivex.annotations.Nullable;

/**
 * sp文件
 */

public class SpHelper {

    private static SpHelper spHelper;
    private static SharedPreferences mSharePreference;

    private SpHelper() {
    }

    public static SpHelper getInstance() {
        if (null == spHelper) {
            synchronized (SpHelper.class) {
                if (null == spHelper) {
                    spHelper = new SpHelper();
                    mSharePreference = ContextHelper.getContext().getSharedPreferences(
                            ContextHelper.getString(R.string.app_name) + "_sp", Context.MODE_PRIVATE);
                }
            }
        }
        return spHelper;
    }

    public static void clearCache() {
        spHelper = null;
    }

    public void put(String key, String value) {
        mSharePreference.edit().putString(key, value).apply();
    }

    public void put(Map<String, String> maps) {
        if (maps == null || maps.size() == 0) return;

        SharedPreferences.Editor editor = mSharePreference.edit();
        for (String key : maps.keySet()) {
            String value = maps.get(key);
            editor.putString(key, value);
        }
        editor.apply();
    }

    public String get(String key, @Nullable String defaultValue) {
        return mSharePreference.getString(key, defaultValue);
    }

    public void remove(String key) {
        mSharePreference.edit().remove(key).apply();
    }
}
