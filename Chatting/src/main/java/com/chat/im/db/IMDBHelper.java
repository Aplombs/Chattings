package com.chat.im.db;

import android.content.Context;

import com.chat.im.db.table.Table_ContactInfo;
import com.chat.im.db.table.Table_MessageBase;
import com.chat.im.db.table.Table_MessagePreView;
import com.chat.im.db.table.Table_WaitAddFriends;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * 数据库帮助类
 */

public class IMDBHelper extends SQLiteOpenHelper {

    /**
     * 创建数据库的表
     */
    private static final String[] SQL_CREATE_ENTRIES = new String[]{
            Table_ContactInfo.TBL_CONTACT_INFO_SQL,//联系人
            Table_WaitAddFriends.TABLE_WAIT_ADD_FRIENDS_INFO_SQL,//待添加好友
            Table_MessagePreView.TBL_MESSAGE_PREVIEW_SQL//消息页签预览消息
    };

    public IMDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (String sql : SQL_CREATE_ENTRIES) {
            sqLiteDatabase.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}