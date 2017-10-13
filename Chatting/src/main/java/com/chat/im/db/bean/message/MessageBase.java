package com.chat.im.db.bean.message;

/**
 * 消息基类
 */

public class MessageBase {

    /**
     * 消息id
     */
    private String messageId;
    /**
     * 消息发送者
     */
    private String messageFrom;
    /**
     * 消息接收者
     */
    private String messageTo;
    /**
     * 消息方向:发送方 接收方
     */
    private int messageDirection;
    /**
     * 消息类型:文本 语音 图片...
     */
    private int messageContentType;
    /**
     * 消息内容
     */
    private String messageContent;
    /**
     * 消息状态
     */
    private int messageStatus;
    /**
     * 附加消息内容消息
     */
    private String attachMessageContent;

    public MessageBase(String messageId, String messageFrom, String messageTo,
                       int messageDirection, int messageContentType, String messageContent,
                       int messageStatus, String attachMessageContent) {
        this.messageId = messageId;
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.messageDirection = messageDirection;
        this.messageContentType = messageContentType;
        this.messageContent = messageContent;
        this.messageStatus = messageStatus;
        this.attachMessageContent = attachMessageContent;
    }

    public MessageBase() {
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public int getMessageDirection() {
        return messageDirection;
    }

    public void setMessageDirection(int messageDirection) {
        this.messageDirection = messageDirection;
    }

    public int getMessageContentType() {
        return messageContentType;
    }

    public void setMessageContentType(int messageContentType) {
        this.messageContentType = messageContentType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getAttachMessageContent() {
        return attachMessageContent;
    }

    public void setAttachMessageContent(String attachMessageContent) {
        this.attachMessageContent = attachMessageContent;
    }
}
