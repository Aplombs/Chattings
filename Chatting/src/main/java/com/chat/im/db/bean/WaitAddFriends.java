package com.chat.im.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 待添加好友
 */
@Entity
public class WaitAddFriends {

    @Id
    private String userId;
    private String region;
    private String phone;
    private String headUri;
    private String nickName;
    private String remarkName;
    private String showName;
    private String showNameLetter;
    private String addFriendAttachMessage;
    private boolean isAdded;

    @Generated(hash = 677675480)
    public WaitAddFriends(String userId, String region, String phone,
                          String headUri, String nickName, String remarkName, String showName,
                          String showNameLetter, String addFriendAttachMessage, boolean isAdded) {
        this.userId = userId;
        this.region = region;
        this.phone = phone;
        this.headUri = headUri;
        this.nickName = nickName;
        this.remarkName = remarkName;
        this.showName = showName;
        this.showNameLetter = showNameLetter;
        this.addFriendAttachMessage = addFriendAttachMessage;
        this.isAdded = isAdded;
    }

    @Generated(hash = 434751742)
    public WaitAddFriends() {
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadUri() {
        return this.headUri;
    }

    public void setHeadUri(String headUri) {
        this.headUri = headUri;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemarkName() {
        return this.remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getShowName() {
        return this.showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowNameLetter() {
        return this.showNameLetter;
    }

    public void setShowNameLetter(String showNameLetter) {
        this.showNameLetter = showNameLetter;
    }

    public String getAddFriendAttachMessage() {
        return this.addFriendAttachMessage;
    }

    public void setAddFriendAttachMessage(String addFriendAttachMessage) {
        this.addFriendAttachMessage = addFriendAttachMessage;
    }

    public boolean getIsAdded() {
        return this.isAdded;
    }

    public void setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }
}