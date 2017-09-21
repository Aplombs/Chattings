package com.chat.im.db.bean.message;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 消息基类
 */
@Entity
public class MessageBase {

    /**
     * 消息id
     */
    @Id
    private String messageId;
    /**
     * 消息发送者
     */
    private long messageFrom;
    /**
     * 消息接收者
     */
    private long messageTo;
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

    @Generated(hash = 1292966825)
    public MessageBase(String messageId, long messageFrom, long messageTo,
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

    @Generated(hash = 682811179)
    public MessageBase() {
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getMessageFrom() {
        return this.messageFrom;
    }

    public void setMessageFrom(long messageFrom) {
        this.messageFrom = messageFrom;
    }

    public long getMessageTo() {
        return this.messageTo;
    }

    public void setMessageTo(long messageTo) {
        this.messageTo = messageTo;
    }

    public int getMessageDirection() {
        return this.messageDirection;
    }

    public void setMessageDirection(int messageDirection) {
        this.messageDirection = messageDirection;
    }

    public int getMessageContentType() {
        return this.messageContentType;
    }

    public void setMessageContentType(int messageContentType) {
        this.messageContentType = messageContentType;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getMessageStatus() {
        return this.messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getAttachMessageContent() {
        return this.attachMessageContent;
    }

    public void setAttachMessageContent(String attachMessageContent) {
        this.attachMessageContent = attachMessageContent;
    }
}
