package com.chat.im.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.chat.im.db.bean.MessagePreView;
import com.chat.im.db.table.Table_MessagePreView;
import com.chat.im.helper.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for table "MESSAGE_PRE_VIEW".
 */
public class MessagePreViewDao extends BaseDao {

    private static MessagePreViewDao mMessagePreViewDao;
    private final String TAG = "[MessagePreViewDao] ";

    private MessagePreViewDao() {
    }

    public static MessagePreViewDao getInstance() {
        if (null == mMessagePreViewDao) {
            synchronized (MessagePreViewDao.class) {
                if (null == mMessagePreViewDao) {
                    mMessagePreViewDao = new MessagePreViewDao();
                }
            }
        }
        return mMessagePreViewDao;
    }

    public void insertMessagePreView(MessagePreView messagePreView) {
        ContentValues contentValues = new ContentValues();
        if (!queryMessagePreViewIsExist(messagePreView.getMessagePreviewId())) {
            contentValues.put(Table_MessagePreView.MESSAGE_PREVIEW_ID, messagePreView.getMessagePreviewId());
            contentValues.put(Table_MessagePreView.USER_NICK_NAME, messagePreView.getUserNickName());
            contentValues.put(Table_MessagePreView.CONTENT_PREVIEW, messagePreView.getContentPreView());
            contentValues.put(Table_MessagePreView.IS_TOP, messagePreView.getIsTop());
            super.insert(Table_MessagePreView.TABLE_NAME, contentValues);
        } else {
            LogHelper.e(TAG + "insertMessagePreView() this MessagePreView existed! --->>" + messagePreView.getContentPreView());
        }
    }

    public boolean deleteMessagePreView(String messagePreViewId) {
        if (!queryMessagePreViewIsExist(messagePreViewId)) {
            super.delete(Table_MessagePreView.TABLE_NAME, Table_MessagePreView.MESSAGE_PREVIEW_ID + " = ?", new String[]{messagePreViewId});
            return true;
        }
        return false;
    }

    public MessagePreView queryMessagePreView(String messagePreViewId) {
        MessagePreView messagePreView = null;
        Cursor cursor = null;
        try {
            String selectionArgs[] = new String[1];
            selectionArgs[0] = String.valueOf(messagePreViewId);
            String selection = Table_MessagePreView.MESSAGE_PREVIEW_ID + " = ?";
            cursor = super.query(Table_MessagePreView.TABLE_NAME, null, selection, selectionArgs, null);
            while (cursor != null && cursor.moveToNext()) {
                messagePreView = initMessagePreView(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return messagePreView;
    }

    public boolean queryMessagePreViewIsExist(String userId) {
        boolean exist = false;
        Cursor cursor = null;
        try {
            String selectionArgs[] = new String[1];
            selectionArgs[0] = String.valueOf(userId);
            String selection = Table_MessagePreView.MESSAGE_PREVIEW_ID + " = ?";
            cursor = super.query(Table_MessagePreView.TABLE_NAME, null, selection, selectionArgs, null);
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

    public List<MessagePreView> queryAllMessagePreView() {
        Cursor cursor = null;
        List<MessagePreView> mList = new ArrayList<>();
        try {
            cursor = query(Table_MessagePreView.TABLE_NAME, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                mList.add(initMessagePreView(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mList;
    }

    private MessagePreView initMessagePreView(Cursor cursor) {
        MessagePreView messagePreView = new MessagePreView();
        messagePreView.setUserNickName(cursor.getString(cursor.getColumnIndex(Table_MessagePreView.USER_NICK_NAME)));
        messagePreView.setContentPreView(cursor.getString(cursor.getColumnIndex(Table_MessagePreView.CONTENT_PREVIEW)));
        messagePreView.setIsTop(cursor.getInt(cursor.getColumnIndex(Table_MessagePreView.IS_TOP)) == 1);
        return messagePreView;
    }
}