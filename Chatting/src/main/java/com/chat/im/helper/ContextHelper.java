package com.chat.im.helper;

import android.content.Context;

/**
 * 上下文对象
 */

public class ContextHelper {

    private static Context mContext;

    private ContextHelper() {
    }

    public static void init(Context context) {
        mContext = context;
    }

    public static void clearCache() {
        mContext = null;
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("context is null,use context must to invoke init() in your application!");
        }
        return mContext;
    }

    public static String getString(int resId) {
        return getContext().getString(resId);
    }

}
