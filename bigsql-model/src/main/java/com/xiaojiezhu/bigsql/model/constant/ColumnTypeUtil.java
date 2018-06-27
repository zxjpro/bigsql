package com.xiaojiezhu.bigsql.model.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class ColumnTypeUtil {
    private static final List<Class<?>> NUMBER_TYPES = Arrays.asList(Integer.class,int.class,Double.class,double.class,Float.class,float.class,Short.class,short.class,Long.class,long.class);

    public static ColumnType getColumnType(Object obj){
        if(obj == null){
            throw new NullPointerException("obj can not be null");
        }else{
            if(obj.getClass() == String.class){
                return ColumnType.VARCHAR;
            }else if(NUMBER_TYPES.contains(obj.getClass())){
                return ColumnType.INT;
            }else{
                throw new RuntimeException("not complete : " + obj.getClass().getName());
            }
        }
    }
}
