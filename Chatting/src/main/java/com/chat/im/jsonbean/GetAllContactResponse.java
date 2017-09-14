package com.chat.im.jsonbean;

import java.util.List;

public class GetAllContactResponse {

    /**
     * code : 200
     * result : [{"displayName":"","message":"手机号:18622222222昵称:的用户请求添加你为好友","status":11,"updatedAt":"2016-01-07T06:22:55.000Z","user":{"id":"i3gRfA1ml","nickname":"nihaoa","portraitUri":""}}]
     * <p>
     * displayName :
     * message : 手机号:18622222222昵称:的用户请求添加你为好友
     * status : 11
     * updatedAt : 2016-01-07T06:22:55.000Z
     * user : {"id":"i3gRfA1ml","nickname":"nihaoa","portraitUri":""}
     * <p>
     * status说明
     * 11: //收到了好友邀请
     * 10: // 发出了好友邀请
     * 21: // 忽略好友邀请
     * 20: // 已是好友
     * 30: // 删除了好友关系
     */
    private int code;

    private List<ResultEntity> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResultEntity> getResult() {
        return result;
    }

    public void setResult(List<ResultEntity> result) {
        this.result = result;
    }

    public static class ResultEntity implements Comparable {

        private String displayName;
        private String message;
        private int status;
        private String updatedAt;
        /**
         * id : i3gRfA1ml
         * nickname : nihaoa
         * portraitUri :
         */

        private UserEntity user;

        public ResultEntity(String displayName, String message, int status, String updatedAt, UserEntity user) {
            this.displayName = displayName;
            this.message = message;
            this.status = status;
            this.updatedAt = updatedAt;
            this.user = user;
        }

        public ResultEntity() {

        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public UserEntity getUser() {
            return user;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        @Override
        public int compareTo(Object another) {
            return 0;
        }

        public static class UserEntity {
            private String id;
            private String nickname;
            private String portraitUri;
            private String region;
            private String phone;

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

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
}