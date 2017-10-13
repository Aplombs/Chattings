package com.chat.im.db.table;

/**
 * 创建联系人信息数据库表
 */

public class Table_ContactInfo {

    public static String TABLE_NAME = "table_contact_info";
    public static String USER_ID = "user_id";
    public static String REGION = "region";
    public static String PHONE = "phone";
    public static String HEAD_URI = "head_uri";
    public static String NICK_NAME = "nick_name";
    public static String REMARK_NAME = "remark_name";
    public static String SHOW_NAME = "show_name";
    public static String SHOW_NAME_LETTER = "show_name_letter";

    /**
     * 建表语句
     */
    public static final String TBL_CONTACT_INFO_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    USER_ID + " TEXT PRIMARY KEY NOT NULL ," + // 0: user_id
                    REGION + " TEXT," + // 1: region
                    PHONE + " TEXT," + // 2: phone
                    HEAD_URI + " TEXT," + // 3: head_uri
                    NICK_NAME + " TEXT," + // 4: nick_name
                    REMARK_NAME + " TEXT," + // 5: remark_name
                    SHOW_NAME + " TEXT," + // 6: show_name
                    SHOW_NAME_LETTER + " TEXT);";//7:show_name_letter
}
