package com.chat.im.jsonbean;


public class GetTokenRequest {

    private String userId;
    private String name;
    private String portraitUri;

    public GetTokenRequest(String userId, String name, String portraitUri) {
        this.userId = userId;
        this.name = name;
        this.portraitUri = portraitUri;
    }

    @Override
    public String toString() {
        //userId=88888888&name=HHHHHH
        return "userId=" + userId
                + "&name=" + name
                + "&portraitUri=" + portraitUri;
    }
}
