/*
    ShengDao Android Client, JsonMananger
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.chat.im.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

import java.util.List;


/**
 * JSON解析管理,使用fastJson
 **/
public class JsonHelper {

    static {
        //保持字段首字母大小写
        TypeUtils.compatibleWithJavaBean = true;
    }

    /**
     * @param json   要转换的json串
     * @param aClass json串转换成的对象
     * @return 将json字符串转换成java对象
     */
    public static <T> T jsonToBean(String json, Class<T> aClass) throws Exception {
        return JSON.parseObject(json, aClass);
    }

    /**
     * @param json   要转换的json串
     * @param aClass json串转换成的对象
     * @return 将json字符串转换成java List对象
     */
    public static <T> List<T> jsonToList(String json, Class<T> aClass) throws Exception {
        return JSON.parseArray(json, aClass);
    }

    /**
     * @param obj 要转换的对象
     * @return 将bean对象转化成json字符串
     */
    public static String beanToJson(Object obj) {
        return JSON.toJSONString(obj);
    }

}
