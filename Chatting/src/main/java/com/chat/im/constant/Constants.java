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
     * content-type
     */
    public static final String CONTENT_TYPE = "application/json";

    /**
     * 编码格式
     */
    public static final String ENCODING = "utf-8";

    /**
     * 地域号:默认86,中国
     */
    public static final String REGION = "86";

    /**
     * 具体访问接口地址
     */
    public static final String URL_CHECK_PHONE_AVAILABLE = "user/check_phone_available";//监测手机号是否可用
    public static final String URL_SEND_CODE = "user/send_code";//发送验证码
    public static final String URL_VERIFY_CODE = "user/verify_code";//验证用户输入验证码是否正确
    public static final String URL_REGISTER = "user/register";//注册
    public static final String URL_LOGIN = "user/login";//登录
    public static final String URL_GET_USER_INFO = "user/";//获取用户信息

    /**
     * 常量字段
     */
    public static final String ACTIVITY_RETURN_PHONE = "phone";
    public static final String ACTIVITY_RETURN_PASSWORD = "password";
    public static final String ACTIVITY_RETURN_NICKNAME = "nickname";
    public static final String ACTIVITY_RETURN_USERID = "userID";

    /**
     * 登录成功,保存用户手机号,用户密码,用户ID
     */
    public static final String SP_LOGIN_USERID = "login_userID";
    public static final String SP_LOGIN_TOKEN = "login_token";
    public static final String SP_LOGIN_PHONE = "login_phone";
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

    /**
     * 失败状态码---比如:是登录失败还是注册失败...
     */
    public static final int FAILURE_SEND_CODE = 1;//失败--发送验证码
    public static final int FAILURE_LOGIN = 2;//失败--登录
    public static final int FAILURE_REGISTER = 3;//失败--注册
    public static final int FAILURE_GET_USER_INFO = 4;//失败--通过ID获取个人信息
    /**
     * 失败原因状态码---比如:是登录密码错误还是用户名错误...
     */
    public static final int FAILURE_TYPE_OTHER = -1;//未知失败原因--异常情况
    public static final int FAILURE_TYPE_PHONE_USED = 1;//失败原因--手机号已被注册
    public static final int FAILURE_TYPE_VERIFY_CODE_WRONG = 2;//失败原因--验证码错误
    public static final int FAILURE_TYPE_VERIFY_CODE_OVERDUE = 3;//失败原因--验证码过去
    public static final int FAILURE_TYPE_LOGIN_PHONE_OR_PASSWORD_WRONG = 4;//失败原因--手机号或者密码错误
}
