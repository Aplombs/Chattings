package com.chat.im.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.chat.im.constant.Constants;
import com.chat.im.db.provider.IMDBCipherProvider;
import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.LogHelper;

/**
 * 通过provider操作数据库的dao
 */

public class BaseDao {

    private final String TAG = "[BaseDao] ";

    public void createTable(String sql) {
        try {
            execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isTableExists(String tableName) {
        Cursor cursor = null;
        boolean exists = false;
        try {
            cursor = rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
            if (cursor != null && cursor.getCount() > 0) {
                exists = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return exists;
    }

    public void execSQL(String sql) {
        int delete = ContextHelper.getContext().getContentResolver().delete(Constants.URI_EXECSQL, sql, null);
        LogHelper.e(TAG + "execSQL:" + sql);
    }

    public Uri insert(String tableName, ContentValues values) {
        return ContextHelper.getContext().getContentResolver().insert(IMDBCipherProvider.getDataBaseUri(tableName), values);
    }

    public int update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        return ContextHelper.getContext().getContentResolver().update(IMDBCipherProvider.getDataBaseUri(tableName), values, whereClause, whereArgs);
    }

    public int delete(String tableName, String whereClause, String[] whereArgs) {
        return ContextHelper.getContext().getContentResolver().delete(IMDBCipherProvider.getDataBaseUri(tableName), whereClause, whereArgs);
    }

    public Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String orderBy) {
        return ContextHelper.getContext().getContentResolver().query(IMDBCipherProvider.getDataBaseUri(tableName), columns, selection, selectionArgs, orderBy);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return ContextHelper.getContext().getContentResolver().query(Constants.URI_RAWQUERY, null, sql, selectionArgs, null);
    }
}