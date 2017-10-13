package com.chat.im.notify;

import android.content.Intent;

import com.chat.im.helper.ContextHelper;

/**
 * 发送广播通知帮助类
 */

public class NotifyHelper {

    private final String TAG = "[NotifyHelper] ";
    private static NotifyHelper mNotifyHelper;

    private NotifyHelper() {
    }

    public static NotifyHelper getInstance() {
        if (null == mNotifyHelper) {
            synchronized (NotifyHelper.class) {
                if (null == mNotifyHelper) {
                    mNotifyHelper = new NotifyHelper();
                }
            }
        }
        return mNotifyHelper;
    }

    public void notifyEvent(int type, Intent data) {
        if (data == null) {
            data = new Intent();
        }
        if (data.hasExtra(NotifyReceiver.EXTRA_NOTIFY_TYPE)) {
            throw new IllegalArgumentException("please send your extra_notify_type!");
        }
        data.setAction(NotifyReceiver.ACTION_NOTIFY);
        data.putExtra(NotifyReceiver.EXTRA_NOTIFY_TYPE, type);
        ContextHelper.getContext().sendBroadcast(data);
    }

    public static void cleanCache() {
        mNotifyHelper = null;
    }
}
