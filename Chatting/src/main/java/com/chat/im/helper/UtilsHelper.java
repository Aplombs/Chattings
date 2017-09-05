package com.chat.im.helper;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */

public class UtilsHelper {

    private static UtilsHelper utilsHelper;
    /**
     * 手机号正则表达式
     */
    private final String MOBILE_PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[7])|(17[0|3|6|7|8]))\\d{8}$";

    private UtilsHelper() {
    }

    public static UtilsHelper getInstance() {
        if (null == utilsHelper) {
            synchronized (UtilsHelper.class) {
                if (null == utilsHelper) {
                    utilsHelper = new UtilsHelper();
                }
            }
        }
        return utilsHelper;
    }

    public static void clearCache() {
        utilsHelper = null;
    }

    /**
     * @param resources 资源对象
     * @return 获取系统statusBar的高度
     */
    public int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean isMobile(String phone) {
        Pattern p = Pattern.compile(MOBILE_PHONE_PATTERN);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * @return 判断网络是否可用
     */
    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextHelper.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public String getFirstLetter(String name) {
        String firstLetter = "#";
        if (TextUtils.isEmpty(name)) {
            return firstLetter;
        }
        String firstStr = name.substring(0, 1);
        if (isNickNameOrRemarkName(firstStr)) {
            String spelling = getSpelling(firstStr);
            if (TextUtils.isEmpty(spelling)) {
                firstLetter = "#";
            } else {
                firstLetter = spelling;
            }
        }

        return firstLetter;
    }

    /**
     * @param str 要判断的名称
     * @return 判断昵称或者备注是否可以排序, 判断的条件就是字符串的第一个字符是否是英文或者中文, 只要不符合就是不能排序
     */
    private boolean isNickNameOrRemarkName(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        //中文
        Pattern patternChinese = Pattern.compile("[\u4e00-\u9fa5]");
        //英文
        Pattern patternEnglish = Pattern.compile("[a-zA-Z]");

        Matcher matcherChinese = patternChinese.matcher(str);
        Matcher matcherEnglish = patternEnglish.matcher(str);
        //只要名称的首个字符是中文或者英文都可以排序
        return matcherChinese.matches() || matcherEnglish.matches();
    }

    /**
     * 获取汉字字符串的第一个字母,默认转大写(唐利涛-T)
     */
    private String getSpelling(String str) {
        StringBuilder sb = new StringBuilder();
        char c = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyinArray != null) {
            sb.append(pinyinArray[0].charAt(0));
        } else {
            sb.append(c);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 所需的4的header
     * App-Key
     * Nonce     -- 随机数
     * Timestamp -- 时间戳
     * Signature -- 数据签名(计算方法:将系统分配的AppSecret Nonce Timestamp三个字符串按先后顺序拼接成一个字符串并进行SHA1哈希计算)
     * <p>
     * App Key     3argexb63dide
     * App Secret  XzftSkOS0qR
     *
     * @return 获取拿token的4个header参数
     */
    public String[] getToken4Headers() {
        String appKey = "3argexb63dide";
        String appSecret = "XzftSkOS0qR";
        String nonce = String.valueOf(new Random().nextLong());
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = sha1(appSecret + nonce + timestamp);
        return new String[]{appKey, nonce, timestamp, signature};
    }

    /**
     * MD5与SHA1都是Hash算法,MD5输出是128位的,SHA1输出是160位的,HA256输出是256位;MD5比SHA1快,SHA1比MD5强度高
     *
     * @param str 要加密的字符串
     * @return 哈希算法之后的值
     */
    private String sha1(String str) {
        try {
            byte[] strBytes = str.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            messageDigest.update(strBytes);
            return bytes2Hex(messageDigest.digest()); //toHexString
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    private String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public String formatPhone(String phone) {
        String str = phone;
        if (phone.length() == 11) {
            String substring1 = phone.substring(0, 3);//187
            String substring2 = phone.substring(3, 7);//3103
            String substring3 = phone.substring(7, 11);//3105
            str = substring1 + "-" + substring2 + "-" + substring3;
        }
        return str;
    }
}
