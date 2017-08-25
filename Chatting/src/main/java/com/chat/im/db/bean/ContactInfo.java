package com.chat.im.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 联系人页签--联系人信息实体
 */
@Entity
public class ContactInfo {

    private String userNickName;
    private String userHeadUri;

    @Generated(hash = 1783484351)
    public ContactInfo(String userNickName, String userHeadUri) {
        this.userNickName = userNickName;
        this.userHeadUri = userHeadUri;
    }

    @Generated(hash = 2019856331)
    public ContactInfo() {
    }

    public String getUserNickName() {
        return this.userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserHeadUri() {
        return this.userHeadUri;
    }

    public void setUserHeadUri(String userHeadUri) {
        this.userHeadUri = userHeadUri;
    }
}
