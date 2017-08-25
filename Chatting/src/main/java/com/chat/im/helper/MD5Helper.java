package com.chat.im.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Helper {

    private static MD5Helper md5Helper;
    /**
     * 全局数组
     **/
    private final String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};

    private MD5Helper() {
    }

    public static MD5Helper getInstance() {
        if (null == md5Helper) {
            synchronized (MD5Helper.class) {
                if (null == md5Helper) {
                    md5Helper = new MD5Helper();
                }
            }
        }
        return md5Helper;
    }

    public static void clearCache() {
        md5Helper = null;
    }

    /**
     * @param bByte 要转换的数组
     * @return 返回形式为数字跟字符串
     */
    private String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    /**
     * @param bByte 要转换的数组
     * @return 转换字节数组为16进制字串
     */
    private String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * @param str 待加密的字符串
     * @return MD5加密
     */
    public String encrypt(String str) {
        String result = null;
        try {
            result = str;
            MessageDigest md = MessageDigest.getInstance("MD5Helper");
            result = byteToString(md.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * @param str       待加密的字符串
     * @param lowerCase 大小写
     * @return MD5加密
     */
    public String encrypt(String str, boolean lowerCase) {
        String result = null;
        try {
            result = str;
            MessageDigest md = MessageDigest.getInstance("MD5Helper");
            result = byteToString(md.digest(str.getBytes()));
            if (lowerCase) {
                result = result.toLowerCase();
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
