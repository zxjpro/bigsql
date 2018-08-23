package com.xiaojiezhu.bigsql.util;

/**
 * time 2018/8/23 9:44
 *
 * @author xiaojie.zhu <br>
 */
public class StringUtil {

    public static String removeBlank1(String str){
        String s = str.replaceAll("\\\n", " ").replaceAll("( )+", " ").replaceAll("\\\\t", "");
        return s;
    }
}
