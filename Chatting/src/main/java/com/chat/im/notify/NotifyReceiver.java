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
    public static final String ACTION_NOTIFY = "com.chat.im.receiver.NotifyReceiver.ACTION_NOTIFY";
    public static final String EXTRA_NOTIFY_TYPE = "extra_notify_type";

    public static final int NOTIFY_TYPE_UPDATE_MESSAGE_PREVIEW = 0;//刷新预览消息页签

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
                int extraNotifyType = intent.getIntExtra(EXTRA_NOTIFY_TYPE, -1);
                NotifyMonitor.getInstance().notifyMonitorEvent(extraNotifyType, intent);
            }
        }
    }
}