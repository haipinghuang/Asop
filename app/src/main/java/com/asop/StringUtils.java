package com.asop;

/**
 * Created by 黄海 on 2017/10/19.
 */

public class StringUtils {
    /**
     * 判断是否为整数
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
