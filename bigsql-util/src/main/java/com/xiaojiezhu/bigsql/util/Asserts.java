package com.xiaojiezhu.bigsql.util;

import java.util.Collection;

/**
 * @author xiaojie.zhu
 */
public class Asserts {

    public static void notNull(Object object,String msg){
        if(object == null){
            throw new NullPointerException(msg);
        }
    }

    public static void collectionIsNotNull(Collection<?> collection,String msg) {
        if(collection == null || collection.size() == 0){
            throw new NullPointerException(msg);
        }
    }

    public static void notEmpty(String str, String msg) {
        if(str == null || str.length() == 0){
            throw new NullPointerException(msg);
        }

    }
}
