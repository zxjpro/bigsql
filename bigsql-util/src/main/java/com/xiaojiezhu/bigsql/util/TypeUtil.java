package com.xiaojiezhu.bigsql.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/4 20:42
 * 说明 ...
 */
public class TypeUtil {
    private static final List<Class<?>> NUMBER_TYPES = Arrays.asList(Integer.class,int.class,Double.class,double.class,Float.class,float.class,Short.class,short.class,Long.class,long.class,Long.class);

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

}
