package com.chat.im.helper;

/**
 * 请求后台接口
 */

public class RequestHelper {

    private static RequestHelper requestHelper;

    private RequestHelper() {
    }

    public static RequestHelper getInstance() {
        if (null == requestHelper) {
            synchronized (RequestHelper.class) {
                if (null == requestHelper) {
                    requestHelper = new RequestHelper();
                }
            }
        }
        return requestHelper;
    }

    public static void clearCache() {
        requestHelper = null;
    }

    public void sendVerificationCode(int phone) {

    }
}
