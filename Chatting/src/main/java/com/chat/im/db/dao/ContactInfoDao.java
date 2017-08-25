package com.chat.im.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.chat.im.db.bean.ContactInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONTACT_INFO".
*/
public class ContactInfoDao extends AbstractDao<ContactInfo, Void> {

    public static final String TABLENAME = "CONTACT_INFO";

    /**
     * Properties of entity ContactInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserNickName = new Property(0, String.class, "userNickName", false, "USER_NICK_NAME");
        public final static Property UserHeadUri = new Property(1, String.class, "userHeadUri", false, "USER_HEAD_URI");
    }


    public ContactInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ContactInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACT_INFO\" (" + //
                "\"USER_NICK_NAME\" TEXT," + // 0: userNickName
                "\"USER_HEAD_URI\" TEXT);"); // 1: userHeadUri
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACT_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ContactInfo entity) {
        stmt.clearBindings();
 
        String userNickName = entity.getUserNickName();
        if (userNickName != null) {
            stmt.bindString(1, userNickName);
        }
 
        String userHeadUri = entity.getUserHeadUri();
        if (userHeadUri != null) {
            stmt.bindString(2, userHeadUri);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ContactInfo entity) {
        stmt.clearBindings();
 
        String userNickName = entity.getUserNickName();
        if (userNickName != null) {
            stmt.bindString(1, userNickName);
        }
 
        String userHeadUri = entity.getUserHeadUri();
        if (userHeadUri != null) {
            stmt.bindString(2, userHeadUri);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public ContactInfo readEntity(Cursor cursor, int offset) {
        ContactInfo entity = new ContactInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // userNickName
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // userHeadUri
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ContactInfo entity, int offset) {
        entity.setUserNickName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserHeadUri(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(ContactInfo entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(ContactInfo entity) {
        return null;
    }

    @Override
    public boolean hasKey(ContactInfo entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
