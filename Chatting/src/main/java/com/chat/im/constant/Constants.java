package com.chat.im.constant;

/**
 * 常量类
 */

public class Constants {

    /**
     * 请求接口域名
     */
    public static final String DOMAIN = "http://api.sealtalk.im/";

    /**
     * 地域号:默认86,中国
     */
    public static final String REGION = "86";

    /**
     * 常量字段
     */
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_HEAD_URI = "user_head_uri";
    public static final String USER_NICK_NAME = "user_nick_name";
    public static final String USER_PASSWORD = "user_password";
    public static final String TAG_INITIATE_GROUP_CHAT_POPUP_WINDOW = "发起群聊";
    public static final String TAG_ADD_FRIEND_POPUP_WINDOW = "添加好友";
    public static final String TAG_SCANNER_POPUP_WINDOW = "扫一扫";
    public static final String TAG_COLLECT_PAYMENT_POPUP_WINDOW = "收付款";
    public static final String TAG_USER_INFO_ME_TAB = "个人信息";
    public static final String TAG_WALLET_ME_TAB = "钱包";
    public static final String TAG_FRIEND_INFO_ME_TAB = "朋友圈";
    public static final String TAG_COLLECTION_ME_TAB = "收藏";
    public static final String TAG_EXPRESSION_ME_TAB = "表情";
    public static final String TAG_SETTING_ME_TAB = "设置";

    /**
     * 登录成功,保存用户手机号,用户密码,用户ID
     */
    public static final String SP_LOGIN_USERID = "login_userID";
    public static final String SP_LOGIN_HEAD_URI = "login_head_uri";
    public static final String SP_LOGIN_TOKEN = "login_token";
    public static final String SP_LOGIN_PHONE = "login_phone";
    public static final String SP_LOGIN_PHONE_REGION = "login_phone_region";
    public static final String SP_LOGIN_PASSWORD = "login_password";
    public static final String SP_LOGIN_NICKNAME = "login_nickname";

    /**
     * ExceptionErrorMessage
     */
    public static final String RESPONSE_RETURN_NULL = "response return null";//返回数据为null

    /**
     * 成功状态码---比如:是登录成功还是注册成功...
     */
    public static final int OK_SEND_CODE = 1;//成功--发送验证码
    public static final int OK_LOGIN = 2;//成功--登录
    public static final int OK_REGISTER = 3;//成功--注册
    public static final int OK_GET_USER_INFO = 4;//成功--通过ID获取个人信息
    public static final int OK_SEARCH_FRIEND = 5;//成功--搜索好友
    public static final int OK_ADD_FRIEND_REQUEST = 6;//成功--发送添加好友请求
    public static final int OK_DELETE_FRIEND_REQUEST = 7;//成功--删除好友

    /**
     * 失败状态码---比如:是登录失败还是注册失败...
     */
    public static final int FAILURE_SEND_CODE = 1;//失败--发送验证码
    public static final int FAILURE_LOGIN = 2;//失败--登录
    public static final int FAILURE_REGISTER = 3;//失败--注册
    public static final int FAILURE_GET_USER_INFO = 4;//失败--通过ID获取个人信息
    public static final int FAILURE_SEARCH_FRIEND = 5;//失败--搜索好友
    public static final int FAILURE_ADD_FRIEND_REQUEST = 6;//失败--发送添加好友请求
    public static final int FAILURE_DELETE_FRIEND_REQUEST = 7;//失败--删除好友

    /**
     * 失败原因状态码---比如:是登录密码错误还是用户名错误...
     */
    public static final int FAILURE_TYPE_OTHER = -1;//未知失败原因--异常情况
    public static final int FAILURE_TYPE_PHONE_USED = 1;//失败原因--手机号已被注册
    public static final int FAILURE_TYPE_VERIFY_CODE_WRONG = 2;//失败原因--验证码错误
    public static final int FAILURE_TYPE_VERIFY_CODE_OVERDUE = 3;//失败原因--验证码过去
    public static final int FAILURE_TYPE_LOGIN_PHONE_OR_PASSWORD_WRONG = 4;//失败原因--手机号或者密码错误
    public static final int FAILURE_TYPE_SEARCH_FRIEND_NOT_EXIST = 5;//失败原因--好友不存在

    /**
     * 数据库名称
     */
    public static final String DB_NAME = "chatting";
}
