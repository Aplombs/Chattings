package com.chat.im.db.bean;

/**
 * 消息页签--预览消息实体
 */
public class MessagePreView {

    private String messagePreviewId;
    private String userNickName;
    private String contentPreView;
    private boolean isTop;
    private String notReadMessageNum;

    public MessagePreView(String messagePreviewId, String userNickName, String contentPreView, String notReadMessageNum, boolean isTop) {
        this.messagePreviewId = messagePreviewId;
        this.userNickName = userNickName;
        this.contentPreView = contentPreView;
        this.notReadMessageNum = notReadMessageNum;
        this.isTop = isTop;
    }

    public MessagePreView() {
    }

    public String getMessagePreviewId() {
        return messagePreviewId;
    }

    public void setMessagePreviewId(String messagePreviewId) {
        this.messagePreviewId = messagePreviewId;
    }

    public String getUserNickName() {
        return this.userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getContentPreView() {
        return this.contentPreView;
    }

    public void setContentPreView(String contentPreView) {
        this.contentPreView = contentPreView;
    }

    public String getNotReadMessageNum() {
        return notReadMessageNum;
    }

    public void setNotReadMessageNum(String notReadMessageNum) {
        this.notReadMessageNum = notReadMessageNum;
    }

    public boolean getIsTop() {
        return this.isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }
}
