package com.example.g_jiaoyan.qq_hx.utils;
//字符串工具类
public class StringUtils {

    //获取首字母
    public static String getInital(String username){
        //汉字转换为拼音
//        HanziToPinyin.getInstance().get(username);

        if(username == null){
            return "";
        }
        return username.substring(0,1).toUpperCase();
    }
}
