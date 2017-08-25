package com.chat.im.jsonbean;


public class GetTokenResponse {

    //"code":200,"userId":"8888ddd","token
    private int code;
    private String userId;
    private String token;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "GetTokenResponse{" +
                "code=" + code +
                ", id='" + userId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

