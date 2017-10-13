package com.chat.im.db.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chat.im.constant.Constants;
import com.chat.im.db.IMDBHelper;
import com.chat.im.db.SqlArguments;
import com.chat.im.helper.ContextHelper;
import com.chat.im.helper.SpHelper;

import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

/**
 * 将数据库保存provider
 */

public class IMDBCipherProvider extends ContentProvider {

    private String mLoginId;
    public IMDBHelper mIMDBHelper;
    private final String TAG = "[IMDBCipherProvider] ";

    @Override
    public boolean onCreate() {
        ContextHelper.init(getContext());
        SQLiteDatabase.loadLibs(ContextHelper.getContext());
        return initDBHelper();
    }

    //初始化数据库帮助类
    private boolean initDBHelper() {
        synchronized (IMDBCipherProvider.class) {
            if (mIMDBHelper != null) mIMDBHelper.close();

            String loginID = SpHelper.getInstance().get(Constants.SP_LOGIN_USERID, "");
            mLoginId = loginID;

            if (TextUtils.isEmpty(loginID)) {
                mIMDBHelper = null;
                return false;
            }

            String dbName = Constants.DB_NAME + loginID + ".db";
            mIMDBHelper = new IMDBHelper(getContext(), dbName, Constants.DB_VERSION);
            return true;
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        String loginID = SpHelper.getInstance().get(Constants.SP_LOGIN_USERID, "");
        if (!loginID.equals(mLoginId)) {
            initDBHelper();
        }

        return mIMDBHelper != null ? mIMDBHelper.getWritableDatabase("") : null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            if (database == null) {
                return null;
            }
            Cursor cursor;
            if (Constants.URI_RAWQUERY.equals(uri)) {//通过传入的URI判断是否执行rawQuery 是的话selection代表的是SQL语句
                cursor = database.rawQuery(selection, selectionArgs);
            } else {
                SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
                cursor = database.query(args.table, projection, args.where, args.args, null, null, sortOrder);
            }
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            if (database == null) {
                return null;
            }
            SqlArguments args = new SqlArguments(uri);
            long id = database.insert(args.table, null, values);
            if (id == -1) {
                return null;
            }
            return ContentUris.withAppendedId(uri, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            if (database == null) {
                return 0;
            }
            if (Constants.URI_EXECSQL.equals(uri)) {//通过传入的URI判断是否执行execSQL 是的话selection代表的是SQL语句
                database.execSQL(selection);
                return 0;
            } else {
                SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
                return database.delete(args.table, args.where, args.args);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            if (database == null) {
                return 0;
            }
            SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
            return database.update(args.table, values, args.where, args.args);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void execSQL(String sql) throws SQLException {
        SQLiteDatabase database = getWritableDatabase();
        if (database == null) {
            return;
        }
        database.execSQL(sql);
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        return super.applyBatch(operations);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    public static Uri getDataBaseUri(String tableName) {
        return Uri.parse("content://" + Constants.AUTHORITY + "/" + tableName);
    }
}
