package com.chat.im.notify;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.Vector;

/**
 * 通知监听
 */

public class NotifyMonitor {

    private static NotifyMonitor mNotifyMonitor;
    private Handler mHandler = new Handler();

    private NotifyMonitor() {
    }

    public static NotifyMonitor getInstance() {
        if (null == mNotifyMonitor) {
            synchronized (NotifyMonitor.class) {
                if (null == mNotifyMonitor) {
                    mNotifyMonitor = new NotifyMonitor();
                }
            }
        }
        return mNotifyMonitor;
    }

    public static void cleanCache() {
        getInstance().upRegisterNotifyMonitorListener();
        mNotifyMonitor = null;
    }

    public void notifyMonitorEvent(final int type, final Object data) {
        for (int i = 0; i < mNotifyListenerList.size(); ) {
            WeakReference<NotifyMonitorListener> wr = mNotifyListenerList.elementAt(i);
            final NotifyMonitorListener monitorListener = wr.get();
            if (monitorListener != null) {
                try {
                    mHandler.post(new Runnable() {
                        public void run() {
                            monitorListener.onUpdate(type, data);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ++i;
            } else {
                mNotifyListenerList.removeElementAt(i);
            }
        }
    }

    private Vector<WeakReference<NotifyMonitorListener>> mNotifyListenerList = new Vector<>();

    public void registerNotifyMonitorListener(NotifyMonitorListener notifyMonitorListener) {
        for (int i = 0; i < mNotifyListenerList.size(); ++i) {
            WeakReference<NotifyMonitorListener> notifyMonitorListenerWeakReference = mNotifyListenerList.elementAt(i);
            NotifyMonitorListener monitorListener = notifyMonitorListenerWeakReference.get();
            if (monitorListener != null && monitorListener.equals(notifyMonitorListener)) {
                return;
            }
        }
        WeakReference<NotifyMonitorListener> monitorListenerWeakReference = new WeakReference<>(notifyMonitorListener);
        mNotifyListenerList.addElement(monitorListenerWeakReference);
    }

    private void upRegisterNotifyMonitorListener() {
        if (mNotifyListenerList == null || mNotifyListenerList.size() == 0)
            return;
        mNotifyListenerList.removeAllElements();
        mNotifyListenerList = null;
    }

    public interface NotifyMonitorListener {
        void onUpdate(int type, Object data);
    }
}