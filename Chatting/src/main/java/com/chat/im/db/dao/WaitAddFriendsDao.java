package com.chat.im.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.chat.im.db.bean.WaitAddFriends;
import com.chat.im.db.table.Table_WaitAddFriends;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for table "WAIT_ADD_FRIENDS".
 */
public class WaitAddFriendsDao extends BaseDao {

    private static WaitAddFriendsDao mWaitAddFriendsDao;
    private final String TAG = "[WaitAddFriendsDao] ";

    private WaitAddFriendsDao() {
    }

    public static WaitAddFriendsDao getInstance() {
        if (null == mWaitAddFriendsDao) {
            synchronized (WaitAddFriendsDao.class) {
                if (null == mWaitAddFriendsDao) {
                    mWaitAddFriendsDao = new WaitAddFriendsDao();
                }
            }
        }
        return mWaitAddFriendsDao;
    }

    public void insertWaitAddFriend(WaitAddFriends waitAddFriend) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table_WaitAddFriends.USER_ID, waitAddFriend.getUserId());
        contentValues.put(Table_WaitAddFriends.REGION, waitAddFriend.getRegion());
        contentValues.put(Table_WaitAddFriends.PHONE, waitAddFriend.getPhone());
        contentValues.put(Table_WaitAddFriends.HEAD_URI, waitAddFriend.getHeadUri());
        contentValues.put(Table_WaitAddFriends.NICK_NAME, waitAddFriend.getNickName());
        contentValues.put(Table_WaitAddFriends.REMARK_NAME, waitAddFriend.getRemarkName());
        contentValues.put(Table_WaitAddFriends.SHOW_NAME, waitAddFriend.getShowName());
        contentValues.put(Table_WaitAddFriends.SHOW_NAME_LETTER, waitAddFriend.getShowNameLetter());
        contentValues.put(Table_WaitAddFriends.ADD_FRIEND_ATTACH_MESSAGE, waitAddFriend.getAddFriendAttachMessage());
        contentValues.put(Table_WaitAddFriends.IS_ADDED, waitAddFriend.getIsAdded());
        super.insert(Table_WaitAddFriends.TABLE_NAME, contentValues);
    }

    public void deleteWaitAddFriend(String userId) {
        try {
            String selectionArgs[] = new String[1];
            selectionArgs[0] = String.valueOf(userId);
            String selection = Table_WaitAddFriends.USER_ID + " = ?";
            super.delete(Table_WaitAddFriends.TABLE_NAME, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllWaitAddFriend() {
        super.execSQL("delete from " + Table_WaitAddFriends.TABLE_NAME);
    }

    public void updateWaitAddFriend(WaitAddFriends waitAddFriend) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table_WaitAddFriends.USER_ID, waitAddFriend.getUserId());
        contentValues.put(Table_WaitAddFriends.REGION, waitAddFriend.getRegion());
        contentValues.put(Table_WaitAddFriends.PHONE, waitAddFriend.getPhone());
        contentValues.put(Table_WaitAddFriends.HEAD_URI, waitAddFriend.getHeadUri());
        contentValues.put(Table_WaitAddFriends.NICK_NAME, waitAddFriend.getNickName());
        contentValues.put(Table_WaitAddFriends.REMARK_NAME, waitAddFriend.getRemarkName());
        contentValues.put(Table_WaitAddFriends.SHOW_NAME, waitAddFriend.getShowName());
        contentValues.put(Table_WaitAddFriends.SHOW_NAME_LETTER, waitAddFriend.getShowNameLetter());
        contentValues.put(Table_WaitAddFriends.ADD_FRIEND_ATTACH_MESSAGE, waitAddFriend.getAddFriendAttachMessage());
        contentValues.put(Table_WaitAddFriends.IS_ADDED, waitAddFriend.getIsAdded());
        super.update(Table_WaitAddFriends.TABLE_NAME, contentValues, Table_WaitAddFriends.USER_ID + " = ? ", new String[]{waitAddFriend.getUserId()});
    }

    public WaitAddFriends queryWaitAddFriend(String userId) {
        WaitAddFriends waitAddFriends = null;
        Cursor cursor = null;
        try {
            String selectionArgs[] = new String[1];
            selectionArgs[0] = String.valueOf(userId);
            String selection = Table_WaitAddFriends.USER_ID + " = ?";
            cursor = super.query(Table_WaitAddFriends.TABLE_NAME, null, selection, selectionArgs, null);
            while (cursor != null && cursor.moveToNext()) {
                waitAddFriends = initWaitAddFriend(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return waitAddFriends;
    }

    public boolean queryWaitAddFriendIsExist(String userId) {
        boolean exist = false;
        Cursor cursor = null;
        try {
            String selectionArgs[] = new String[1];
            selectionArgs[0] = String.valueOf(userId);
            String selection = Table_WaitAddFriends.USER_ID + " = ?";
            cursor = super.query(Table_WaitAddFriends.TABLE_NAME, null, selection, selectionArgs, null);
            if (cursor != null && cursor.getCount() > 0) {
                exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return exist;
    }

    public List<WaitAddFriends> queryAllWaitAddFriend() {
        Cursor cursor = null;
        try {
            List<WaitAddFriends> mList = new ArrayList<>();
            cursor = query(Table_WaitAddFriends.TABLE_NAME, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                mList.add(initWaitAddFriend(cursor));
            }
            return mList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private WaitAddFriends initWaitAddFriend(Cursor cursor) {
        WaitAddFriends waitAddFriends = new WaitAddFriends();
        waitAddFriends.setUserId(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.USER_ID)));
        waitAddFriends.setRegion(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.REGION)));
        waitAddFriends.setPhone(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.PHONE)));
        waitAddFriends.setHeadUri(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.HEAD_URI)));
        waitAddFriends.setNickName(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.NICK_NAME)));
        waitAddFriends.setRemarkName(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.REMARK_NAME)));
        waitAddFriends.setShowName(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.SHOW_NAME)));
        waitAddFriends.setShowNameLetter(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.SHOW_NAME_LETTER)));
        waitAddFriends.setAddFriendAttachMessage(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.ADD_FRIEND_ATTACH_MESSAGE)));
        waitAddFriends.setIsAdded(cursor.getString(cursor.getColumnIndex(Table_WaitAddFriends.IS_ADDED)).equals("1"));
        return waitAddFriends;
    }
}