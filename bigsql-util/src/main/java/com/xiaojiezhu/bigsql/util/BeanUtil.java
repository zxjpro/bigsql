package com.xiaojiezhu.bigsql.util;

import java.util.*;

/**
 * @author xiaojie.zhu
 */
public class BeanUtil {

    public static boolean hasEmpty(List<Object> objects){
        if(objects == null || objects.size() == 0){
            return true;
        }
        for (Object object : objects) {
            if(object == null){
                return true;
            }
        }

        return false;
    }

    public static <T> List<T> toList(Set<?> objects){
        if(objects == null || objects.size() == 0){
            return null;
        }else{
            List<T> list = new ArrayList<>(objects.size());
            for (Object object : objects) {
                list.add((T) object);
            }
            return list;
        }
    }

    public static <T> Set<T> toSet(Object[] objects){
        if(objects == null || objects.length == 0){
            return null;
        }else{
            Set<T> set = new HashSet<>(objects.length);
            for (Object object : objects) {
                set.add((T) object);
            }
            return set;
        }
    }


    public static boolean isEmpty(Collection<?> collection){
        if(collection == null || collection.size() == 0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isStringEmpty(Object object){
        if(object == null || "".equals(object.toString().trim())){
            return true;
        }else{
            return false;
        }
    }

    public static long count(Collection<Integer> integers){
        if(integers == null || integers.size() == 0){
            return 0;
        }else{
            long count = 0;
            for (Integer integer : integers) {
                if(integer != null){
                    count += integer;
                }
            }
            return count;
        }
    }

}
