package com.chat.im.jsonbean;

/**
 * 通过手机号搜索好友
 */
public class GetUserInfoByPhoneResponse {


    /**
     * code : 200
     * result : {"id":"10YVscJI3","nickname":"阿明","portraitUri":""}
     */

    private int code;
    /**
     * id : 10YVscJI3
     * nickname : 阿明
     * portraitUri :
     */

    private ResultEntity result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        private String id;
        private String nickname;
        private String portraitUri;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }
    }
}
