package com.chat.im.jsonbean;


public class DeleteFriendRequest {
    private String friendId;

    public DeleteFriendRequest(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
