package com.chat.im.helper;

import com.chat.im.db.dao.ContactInfoDao;
import com.chat.im.db.dao.MessageBaseDao;
import com.chat.im.db.dao.MessagePreViewDao;
import com.chat.im.db.dao.WaitAddFriendsDao;

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

    public static void cleanCache() {
        dbHelper = null;
    }

    public ContactInfoDao getContactDao() {
        return ContactInfoDao.getInstance();
    }

    public MessageBaseDao getMessageBaseDao() {
        return MessageBaseDao.getInstance();
    }

    public MessagePreViewDao getMessagePreViewDao() {
        return MessagePreViewDao.getInstance();
    }

    public WaitAddFriendsDao getWaitAddFriendsDao() {
        return WaitAddFriendsDao.getInstance();
    }

    public void closeDB() {
    }
}
