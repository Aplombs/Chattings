package com.chat.im.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 待添加好友
 */
@Entity
public class WaitAddFriends extends ContactInfo {

    private String addFriendAttachMessage;

    public WaitAddFriends(String userId, String region, String phone, String headUri, String nickName, String remarkName, String showName, String showNameLetter, String addFriendAttachMessage) {
        super(userId, region, phone, headUri, nickName, remarkName, showName, showNameLetter);
        this.addFriendAttachMessage = addFriendAttachMessage;
    }

    @Generated(hash = 1530789906)
    public WaitAddFriends(String addFriendAttachMessage) {
        this.addFriendAttachMessage = addFriendAttachMessage;
    }

    @Generated(hash = 434751742)
    public WaitAddFriends() {
    }

    public String getAddFriendAttachMessage() {
        return this.addFriendAttachMessage;
    }

    public void setAddFriendAttachMessage(String addFriendAttachMessage) {
        this.addFriendAttachMessage = addFriendAttachMessage;
    }
}
