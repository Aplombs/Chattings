package com.chat.im.notify;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 处理本地发送的广播
 */

public class NotifyReceiver extends BroadcastReceiver {

    private final String TAG = "[NotifyReceiver] ";
    /**
     * 哪种功能
     */
    public static final String EXTRA_NOTIFY_FUNCTION = "extra_notify_function";
    public static final String ACTION_NOTIFY = "com.chat.im.receiver.NotifyReceiver.ACTION_NOTIFY";

    /**
     * 刷新预览消息页签-刷新item未读数
     */
    public static final int NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_ITEM_NOT_READ = 1;
    /**
     * 刷新预览消息页签-刷新item最后一条预览消息内容
     */
    public static final int NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_ITEM_CONTENT = 2;
    /**
     * 刷新预览消息页签-刷新tab未读数
     */
    public static final int NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW_TAB_NOT_READ = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, NotifyService.class);
        context.startService(intent);
    }

    public static class NotifyService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            dealNotify(intent);
            return super.onStartCommand(intent, flags, startId);
        }

        private void dealNotify(Intent intent) {
            if (intent != null && ACTION_NOTIFY.equals(intent.getAction())) {
                int extraNotifyFunction = intent.getIntExtra(EXTRA_NOTIFY_FUNCTION, -1);
                NotifyMonitor.getInstance().notifyMonitorEvent(extraNotifyFunction, intent);
            }
        }
    }
}