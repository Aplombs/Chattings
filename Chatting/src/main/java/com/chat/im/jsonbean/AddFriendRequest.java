package com.chat.im.jsonbean;

public class AddFriendRequest {
    private String friendId;
    private String message;

    public AddFriendRequest(String userid, String addFriendMessage) {
        this.message = addFriendMessage;
        this.friendId = userid;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
