package com.chat.im.db.table;

/**
 * 创建待添加联系人信息数据库表
 */

public class Table_WaitAddFriends extends Table_ContactInfo {

    public static String TABLE_NAME = "table_wait_add_friends";
    public static String IS_ADDED = "is_added";
    public static String ADD_FRIEND_ATTACH_MESSAGE = "add_friend_attach_message";
    ;

    /**
     * 建表语句
     */
    public static final String TABLE_WAIT_ADD_FRIENDS_INFO_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + USER_ID + "TEXT PRIMARY KEY NOT NULL ," + // 0: userId
                    REGION + " TEXT," + // 1: region
                    PHONE + " TEXT," + // 2: phone
                    HEAD_URI + " TEXT," + // 3: headUri
                    NICK_NAME + " TEXT," + // 4: nickName
                    REMARK_NAME + " TEXT," + // 5: remarkName
                    SHOW_NAME + " TEXT," + // 6: showName
                    SHOW_NAME_LETTER + " TEXT," + // 7: showNameLetter
                    ADD_FRIEND_ATTACH_MESSAGE + " TEXT," + // 8: addFriendAttachMessage
                    IS_ADDED + " Text );"; // 9: isAdded 1已添加 -1未添加
}
