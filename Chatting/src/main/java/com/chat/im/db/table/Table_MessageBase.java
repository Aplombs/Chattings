package com.chat.im.db.table;

/**
 * 创建联系人信息数据库表
 */

public class Table_MessageBase {

    public static String TABLE_NAME = "table_message_base_";

    public static String MESSAGE_ID = "message_id";
    public static String MESSAGE_FROM = "message_from";
    public static String MESSAGE_TO = "message_to";
    public static String MESSAGE_DIRECTION = "message_direction";
    public static String MESSAGE_CONTENT_TYPE = "message_content_type";
    public static String MESSAGE_CONTENT = "message_content";
    public static String MESSAGE_STATUS = "message_status";
    public static String ATTACH_MESSAGE_CONTENT = "attach_message_content";

    public static final String TBL_MESSAGE_BASE_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "%s" + " (" +
                    MESSAGE_ID + " TEXT PRIMARY KEY NOT NULL ," + // 0: messageId
                    MESSAGE_FROM + " TEXT ," + // 1: messageFrom
                    MESSAGE_TO + " TEXT ," + // 2: messageTo
                    MESSAGE_DIRECTION + " INTEGER NOT NULL ," + // 3: messageDirection
                    MESSAGE_CONTENT_TYPE + " INTEGER NOT NULL ," + // 4: messageContentType
                    MESSAGE_CONTENT + " TEXT," + // 5: messageContent
                    MESSAGE_STATUS + " INTEGER NOT NULL ," + // 6: messageStatus
                    ATTACH_MESSAGE_CONTENT + " TEXT);"; // 7: attachMessageContent
}
