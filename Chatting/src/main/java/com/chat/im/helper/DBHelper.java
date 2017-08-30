package com.chat.im.helper;

import com.chat.im.constant.Constants;
import com.chat.im.db.dao.DaoMaster;
import com.chat.im.db.dao.DaoSession;

/**
 * 数据库帮助类
 */

public class DBHelper {

    private static DBHelper dbHelper;

    private DBHelper() {
    }

    public static DBHelper getInstance() {
        if (null == dbHelper) {
            synchronized (DBHelper.class) {
                if (null == dbHelper) {
                    dbHelper = new DBHelper();
                }
            }
        }
        return dbHelper;
    }

    public DaoSession getDaoSession() {
        String loginID = SpHelper.getInstance().get(Constants.SP_LOGIN_USERID, "");
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(ContextHelper.getContext(),
                Constants.DB_NAME + "_" + loginID);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        return daoMaster.newSession();
    }
}
