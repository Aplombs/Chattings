package com.chat.im.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.chat.im.db.bean.WaitAddFriends;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WAIT_ADD_FRIENDS".
*/
public class WaitAddFriendsDao extends AbstractDao<WaitAddFriends, String> {

    public static final String TABLENAME = "WAIT_ADD_FRIENDS";

    /**
     * Properties of entity WaitAddFriends.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, String.class, "userId", true, "USER_ID");
        public final static Property Region = new Property(1, String.class, "region", false, "REGION");
        public final static Property Phone = new Property(2, String.class, "phone", false, "PHONE");
        public final static Property HeadUri = new Property(3, String.class, "headUri", false, "HEAD_URI");
        public final static Property NickName = new Property(4, String.class, "nickName", false, "NICK_NAME");
        public final static Property RemarkName = new Property(5, String.class, "remarkName", false, "REMARK_NAME");
        public final static Property ShowName = new Property(6, String.class, "showName", false, "SHOW_NAME");
        public final static Property ShowNameLetter = new Property(7, String.class, "showNameLetter", false, "SHOW_NAME_LETTER");
        public final static Property AddFriendAttachMessage = new Property(8, String.class, "addFriendAttachMessage", false, "ADD_FRIEND_ATTACH_MESSAGE");
        public final static Property IsAdded = new Property(9, boolean.class, "isAdded", false, "IS_ADDED");
    }


    public WaitAddFriendsDao(DaoConfig config) {
        super(config);
    }
    
    public WaitAddFriendsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WAIT_ADD_FRIENDS\" (" + //
                "\"USER_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: userId
                "\"REGION\" TEXT," + // 1: region
                "\"PHONE\" TEXT," + // 2: phone
                "\"HEAD_URI\" TEXT," + // 3: headUri
                "\"NICK_NAME\" TEXT," + // 4: nickName
                "\"REMARK_NAME\" TEXT," + // 5: remarkName
                "\"SHOW_NAME\" TEXT," + // 6: showName
                "\"SHOW_NAME_LETTER\" TEXT," + // 7: showNameLetter
                "\"ADD_FRIEND_ATTACH_MESSAGE\" TEXT," + // 8: addFriendAttachMessage
                "\"IS_ADDED\" INTEGER NOT NULL );"); // 9: isAdded
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WAIT_ADD_FRIENDS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, WaitAddFriends entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(2, region);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String headUri = entity.getHeadUri();
        if (headUri != null) {
            stmt.bindString(4, headUri);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(5, nickName);
        }
 
        String remarkName = entity.getRemarkName();
        if (remarkName != null) {
            stmt.bindString(6, remarkName);
        }
 
        String showName = entity.getShowName();
        if (showName != null) {
            stmt.bindString(7, showName);
        }
 
        String showNameLetter = entity.getShowNameLetter();
        if (showNameLetter != null) {
            stmt.bindString(8, showNameLetter);
        }
 
        String addFriendAttachMessage = entity.getAddFriendAttachMessage();
        if (addFriendAttachMessage != null) {
            stmt.bindString(9, addFriendAttachMessage);
        }
        stmt.bindLong(10, entity.getIsAdded() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, WaitAddFriends entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(2, region);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String headUri = entity.getHeadUri();
        if (headUri != null) {
            stmt.bindString(4, headUri);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(5, nickName);
        }
 
        String remarkName = entity.getRemarkName();
        if (remarkName != null) {
            stmt.bindString(6, remarkName);
        }
 
        String showName = entity.getShowName();
        if (showName != null) {
            stmt.bindString(7, showName);
        }
 
        String showNameLetter = entity.getShowNameLetter();
        if (showNameLetter != null) {
            stmt.bindString(8, showNameLetter);
        }
 
        String addFriendAttachMessage = entity.getAddFriendAttachMessage();
        if (addFriendAttachMessage != null) {
            stmt.bindString(9, addFriendAttachMessage);
        }
        stmt.bindLong(10, entity.getIsAdded() ? 1L: 0L);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public WaitAddFriends readEntity(Cursor cursor, int offset) {
        WaitAddFriends entity = new WaitAddFriends( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // region
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // phone
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // headUri
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // nickName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // remarkName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // showName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // showNameLetter
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // addFriendAttachMessage
            cursor.getShort(offset + 9) != 0 // isAdded
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, WaitAddFriends entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setRegion(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPhone(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHeadUri(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNickName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRemarkName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setShowName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setShowNameLetter(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAddFriendAttachMessage(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIsAdded(cursor.getShort(offset + 9) != 0);
     }
    
    @Override
    protected final String updateKeyAfterInsert(WaitAddFriends entity, long rowId) {
        return entity.getUserId();
    }
    
    @Override
    public String getKey(WaitAddFriends entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(WaitAddFriends entity) {
        return entity.getUserId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
