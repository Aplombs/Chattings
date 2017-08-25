package com.chat.im.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 消息页签--预览消息实体
 */
@Entity
public class MessagePreView {

    private String userNickName;
    private String contentPreView;
    private boolean isTop;

    @Generated(hash = 49387675)
    public MessagePreView(String userNickName, String contentPreView,
                          boolean isTop) {
        this.userNickName = userNickName;
        this.contentPreView = contentPreView;
        this.isTop = isTop;
    }

    @Generated(hash = 1584209322)
    public MessagePreView() {
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

    public boolean getIsTop() {
        return this.isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }
}
