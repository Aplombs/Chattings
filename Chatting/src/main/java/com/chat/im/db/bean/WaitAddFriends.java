package com.chat.im.db.bean;

/**
 * 待添加好友
 */
public class WaitAddFriends extends ContactInfo {

    private boolean isAdded;//1已添加 -1未添加
    private String addFriendAttachMessage;

    public WaitAddFriends(String userId, String region, String phone,
                          String headUri, String nickName, String remarkName, String showName,
                          String showNameLetter, String addFriendAttachMessage, boolean isAdded) {
        super(userId, region, phone, headUri, nickName, remarkName, showName, showNameLetter);
        this.addFriendAttachMessage = addFriendAttachMessage;
        this.isAdded = isAdded;
    }

    public WaitAddFriends() {
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