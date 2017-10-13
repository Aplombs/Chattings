package com.chat.im.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.chat.im.constant.Constants;
import com.chat.im.db.bean.ContactInfo;
import com.chat.im.db.table.Table_ContactInfo;
import com.chat.im.helper.LogHelper;
import com.chat.im.helper.SpHelper;
import com.chat.im.helper.UtilsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for table "CONTACT_NFO".
 */

public class ContactInfoDao extends BaseDao {

    private static ContactInfoDao mContactInfoDao;
    private final String TAG = "[ContactInfoDao] ";

    private ContactInfoDao() {
    }

    public static ContactInfoDao getInstance() {
        if (null == mContactInfoDao) {
            synchronized (ContactInfoDao.class) {
                if (null == mContactInfoDao) {
                    mContactInfoDao = new ContactInfoDao();
                }
            }
        }
        return mContactInfoDao;
    }

    public void insertContact(ContactInfo contactInfo) {
        ContentValues contentValues = new ContentValues();
        if (!queryContactIsExist(contactInfo.getUserId())) {
            contentValues.put(Table_ContactInfo.USER_ID, contactInfo.getUserId());
            contentValues.put(Table_ContactInfo.REGION, contactInfo.getRegion());
            contentValues.put(Table_ContactInfo.PHONE, contactInfo.getPhone());
            contentValues.put(Table_ContactInfo.HEAD_URI, contactInfo.getHeadUri());
            contentValues.put(Table_ContactInfo.NICK_NAME, contactInfo.getNickName());
            contentValues.put(Table_ContactInfo.REMARK_NAME, contactInfo.getRemarkName());
            contentValues.put(Table_ContactInfo.SHOW_NAME, contactInfo.getShowName());
            contentValues.put(Table_ContactInfo.SHOW_NAME_LETTER, contactInfo.getShowNameLetter());
            super.insert(Table_ContactInfo.TABLE_NAME, contentValues);
        } else {
            LogHelper.e(TAG + "insertContact() this contact existed! --->>" + contactInfo.getUserId());
        }
    }

    public boolean deleteContact(String userID) {
        if (!queryContactIsExist(userID)) {
            super.delete(Table_ContactInfo.TABLE_NAME, Table_ContactInfo.USER_ID + " = ?", new String[]{userID});
            return true;
        }
        return false;
    }

    public ContactInfo queryContact(String userId) {
        ContactInfo contactInfo = null;
        Cursor cursor = null;
        try {
            String selectionArgs[] = new String[1];
            selectionArgs[0] = String.valueOf(userId);
            String selection = Table_ContactInfo.USER_ID + " = ?";
            cursor = super.query(Table_ContactInfo.TABLE_NAME, null, selection, selectionArgs, null);
            while (cursor != null && cursor.moveToNext()) {
                contactInfo = initContact(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contactInfo;
    }

    public boolean queryContactIsExist(String userId) {
        boolean exist = false;
        Cursor cursor = null;
        try {
            String selectionArgs[] = new String[1];
            selectionArgs[0] = String.valueOf(userId);
            String selection = Table_ContactInfo.USER_ID + " = ?";
            cursor = super.query(Table_ContactInfo.TABLE_NAME, null, selection, selectionArgs, null);
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

    public List<ContactInfo> queryAllContactList() {
        Cursor cursor = null;
        List<ContactInfo> mList = new ArrayList<>();
        try {
            cursor = query(Table_ContactInfo.TABLE_NAME, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                mList.add(initContact(cursor));
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

    public void insertMySelf() {
        String userId = SpHelper.getInstance().get(Constants.SP_LOGIN_USERID, "");
        String region = SpHelper.getInstance().get(Constants.SP_LOGIN_PHONE_REGION, "");
        String phone = SpHelper.getInstance().get(Constants.SP_LOGIN_PHONE, "");
        String userHeadUri = SpHelper.getInstance().get(Constants.SP_LOGIN_HEAD_URI, "");
        String nickname = SpHelper.getInstance().get(Constants.SP_LOGIN_NICKNAME, "");
        String showNameLetter = UtilsHelper.getInstance().getFirstLetter(nickname);
        if (!queryContactIsExist(userId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Table_ContactInfo.USER_ID, userId);
            contentValues.put(Table_ContactInfo.REGION, region);
            contentValues.put(Table_ContactInfo.PHONE, phone);
            contentValues.put(Table_ContactInfo.HEAD_URI, userHeadUri);
            contentValues.put(Table_ContactInfo.NICK_NAME, nickname);
            contentValues.put(Table_ContactInfo.REMARK_NAME, "");
            contentValues.put(Table_ContactInfo.SHOW_NAME, nickname);
            contentValues.put(Table_ContactInfo.SHOW_NAME_LETTER, showNameLetter);
            ContactInfoDao.getInstance().insert(Table_ContactInfo.TABLE_NAME, contentValues);
        } else {
            LogHelper.e(TAG + "insertMySelf() this contact existed! --->>" + userId);
        }
    }

    private ContactInfo initContact(Cursor cursor) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUserId(cursor.getString(cursor.getColumnIndex(Table_ContactInfo.USER_ID)));
        contactInfo.setRegion(cursor.getString(cursor.getColumnIndex(Table_ContactInfo.REGION)));
        contactInfo.setPhone(cursor.getString(cursor.getColumnIndex(Table_ContactInfo.PHONE)));
        contactInfo.setHeadUri(cursor.getString(cursor.getColumnIndex(Table_ContactInfo.HEAD_URI)));
        contactInfo.setNickName(cursor.getString(cursor.getColumnIndex(Table_ContactInfo.NICK_NAME)));
        contactInfo.setRemarkName(cursor.getString(cursor.getColumnIndex(Table_ContactInfo.REMARK_NAME)));
        contactInfo.setShowName(cursor.getString(cursor.getColumnIndex(Table_ContactInfo.SHOW_NAME)));
        contactInfo.setShowNameLetter(cursor.getString(cursor.getColumnIndex(Table_ContactInfo.SHOW_NAME_LETTER)));
        return contactInfo;
    }
}