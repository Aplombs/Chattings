package com.chat.im.application;

/**
 * 全局常用数据
 */

public class IMApp {

    private static IMApp imApp;
    //当前登录用户ID
    public String mLoginID;

    private IMApp() {
    }

    public static IMApp getInstance() {
        if (null == imApp) {
            synchronized (IMApp.class) {
                if (null == imApp) {
                    imApp = new IMApp();
                }
            }
        }
        return imApp;
    }
}
