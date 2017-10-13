package com.chat.im.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.chat.im.db.bean.message.MessageBase;
import com.chat.im.db.table.Table_MessageBase;
import com.chat.im.helper.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for table "MESSAGE_BASE".
 */
public class MessageBaseDao extends BaseDao {

    private static MessageBaseDao mMessageBaseDao;
    private final String TAG = "[MessageBaseDao] ";

    private MessageBaseDao() {
    }

    public static MessageBaseDao getInstance() {
        if (null == mMessageBaseDao) {
            synchronized (MessageBaseDao.class) {
                if (null == mMessageBaseDao) {
                    mMessageBaseDao = new MessageBaseDao();
                }
            }
        }
        return mMessageBaseDao;
    }

    public void insertMessage(MessageBase messageBase) {
        ContentValues contentValues = new ContentValues();
        if (!queryMessageIsExist(messageBase.getMessageTo(), messageBase.getMessageId())) {
            contentValues.put(Table_MessageBase.MESSAGE_ID, messageBase.getMessageId());
            contentValues.put(Table_MessageBase.MESSAGE_FROM, messageBase.getMessageFrom());
            contentValues.put(Table_MessageBase.MESSAGE_TO, messageBase.getMessageTo());
            contentValues.put(Table_MessageBase.MESSAGE_DIRECTION, messageBase.getMessageDirection());
            contentValues.put(Table_MessageBase.MESSAGE_CONTENT_TYPE, messageBase.getMessageContentType());
            contentValues.put(Table_MessageBase.MESSAGE_CONTENT, messageBase.getMessageContent());
            contentValues.put(Table_MessageBase.MESSAGE_STATUS, messageBase.getMessageStatus());
            contentValues.put(Table_MessageBase.ATTACH_MESSAGE_CONTENT, messageBase.getAttachMessageContent());
            String tableName = Table_MessageBase.TABLE_NAME + messageBase.getMessageTo();
            if (!isTableExists(tableName)) {
                createTable(String.format(Table_MessageBase.TBL_MESSAGE_BASE_SQL, messageBase.getMessageTo()));
            }
            super.insert(tableName, contentValues);
        } else {
            LogHelper.e(TAG + "insertMessage() this Message existed! --->>" + messageBase.getMessageId());
        }
    }

    public boolean deleteMessage(String userId, String messagePreViewId) {
        if (!queryMessageIsExist(userId, messagePreViewId)) {
            super.delete(Table_MessageBase.TABLE_NAME + userId, Table_MessageBase.MESSAGE_ID + " = ?", new String[]{messagePreViewId});
            return true;
        }
        return false;
    }

    public MessageBase queryMessage(String userId, String messageId) {
        MessageBase messageBase = null;
        Cursor cursor = null;
        try {
            String selectionArgs[] = new String[1];
            selectionArgs[0] = String.valueOf(messageId);
            String selection = Table_MessageBase.MESSAGE_ID + " = ?";
            cursor = super.query(Table_MessageBase.TABLE_NAME + userId, null, selection, selectionArgs, null);
            while (cursor != null && cursor.moveToNext()) {
                messageBase = initMessageBase(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return messageBase;
    }

    public boolean queryMessageIsExist(String userId, String messageId) {
        boolean exist = false;
        Cursor cursor = null;
        try {
            String selection = Table_MessageBase.MESSAGE_ID + " = ?";
            cursor = super.query(Table_MessageBase.TABLE_NAME + userId, null, selection, new String[]{messageId}, null);
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

    public List<MessageBase> queryAllMessages(String userId) {
        Cursor cursor = null;
        List<MessageBase> mList = new ArrayList<>();
        String tableName = Table_MessageBase.TABLE_NAME + userId;
        try {
            cursor = query(tableName, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                mList.add(initMessageBase(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return mList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mList;
    }

    private MessageBase initMessageBase(Cursor cursor) {
        MessageBase messageBase = new MessageBase();
        messageBase.setMessageId(cursor.getString(cursor.getColumnIndex(Table_MessageBase.MESSAGE_ID)));
        messageBase.setMessageFrom(cursor.getString(cursor.getColumnIndex(Table_MessageBase.MESSAGE_FROM)));
        messageBase.setMessageTo(cursor.getString(cursor.getColumnIndex(Table_MessageBase.MESSAGE_TO)));
        messageBase.setMessageDirection(cursor.getInt(cursor.getColumnIndex(Table_MessageBase.MESSAGE_DIRECTION)));
        messageBase.setMessageContentType(cursor.getInt(cursor.getColumnIndex(Table_MessageBase.MESSAGE_CONTENT_TYPE)));
        messageBase.setMessageContent(cursor.getString(cursor.getColumnIndex(Table_MessageBase.MESSAGE_CONTENT)));
        messageBase.setMessageStatus(cursor.getInt(cursor.getColumnIndex(Table_MessageBase.MESSAGE_STATUS)));
        messageBase.setAttachMessageContent(cursor.getString(cursor.getColumnIndex(Table_MessageBase.ATTACH_MESSAGE_CONTENT)));
        return messageBase;
    }
}