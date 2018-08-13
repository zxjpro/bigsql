package com.xiaojiezhu.bigsql.util;

import com.sun.org.apache.bcel.internal.generic.RET;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/4 20:42
 * 说明 ...
 */
public class TypeUtil {
    private static final List<Class<?>> NUMBER_TYPES = Arrays.asList(Integer.class,int.class,Double.class,double.class,Float.class,float.class,Short.class,short.class,Long.class,long.class,Long.class);
    public static final String YMD = "([\\d]{4}?)-([\\d]+?)-([\\d]+?)";
    public static final String YMDHMS = "([\\d]{4}?)-([\\d]+?)-([\\d]+?) ([\\d]+?):([\\d]+?):([\\d]+)";

    public static boolean isNumber(Object obj){
        Class<?> aClass = obj.getClass();
        return NUMBER_TYPES.contains(aClass);
    }

    public static boolean isLong(Object obj){
        Class<?> aClass = obj.getClass();
        if(aClass == Long.class || aClass == long.class){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isInt(Object obj){
        Class<?> aClass = obj.getClass();
        if(aClass == Integer.class || aClass == int.class){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isDate(Object value) {
        if(value == null){
            return false;
        }else{
            return value instanceof Date;
        }
    }

    /**
     * parse any str to date
     * @param str
     * @return
     */
    public static Date parseDate(String str) throws ParseException {
        if(str.matches(YMD)){
            return DateUtils.parse(str , "yyyy-MM-dd");
        }else if(str.matches(YMDHMS)){
            return DateUtils.parse(str ,"yyyy-MM-dd HH:mm:ss");
        }else{
            throw new ParseException(str + " can not parse java.util.Date",0);
        }
    }
}
