package com.chat.im.db.table;

/**
 * 创建联系人信息数据库表
 */

public class Table_MessagePreView {

    public static String TABLE_NAME = "table_message_preview";

    public static String MESSAGE_PREVIEW_ID = "message_preview_id";
    public static String USER_NICK_NAME = "user_nick_name";
    public static String CONTENT_PREVIEW = "content_preview";
    public static String NOT_READ_MESSAGE_NUM = "not_read_message_num";
    public static String IS_TOP = "is_top";//1置顶 -1非置顶

    /**
     * 建表语句
     */
    public static final String TBL_MESSAGE_PREVIEW_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    MESSAGE_PREVIEW_ID + " TEXT PRIMARY KEY NOT NULL ," + // 0: user_id
                    USER_NICK_NAME + " TEXT," + // 0: userNickName
                    CONTENT_PREVIEW + " TEXT," + // 1: contentPreView
                    NOT_READ_MESSAGE_NUM + " TEXT," + // 2: notReadMessageNum
                    IS_TOP + " INTEGER DEFAULT -1 );"; // 3: isTop
}
