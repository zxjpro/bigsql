package com.xiaojiezhu.bigsql.core.type;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * time 2018/7/4 16:53
 *
 * @author xiaojie.zhu <br>
 */
public class TypeFactory {

    public static Type<?> getType(Object obj){
        if(obj == null){
            return NullType.getInstance();
        }else{
            Class<?> clazz = obj.getClass();
            if(Integer.class == clazz || int.class == clazz){
                return new IntType((Integer) obj);
            }else if(Double.class == clazz || double.class == clazz || float.class == clazz || Float.class == clazz){
                return new DoubleType((Double) obj);
            }else if(obj instanceof Date){
                return new DatetimeType((Date) obj);
            }else if(String.class == clazz){
                return new VarcharType((String) obj);
            }else if(long.class == clazz || Long.class == clazz){
                return new BigintType((Long) obj);
            }else if(Boolean.class == clazz || boolean.class == clazz){
                return new TinyintType((boolean) obj);
            }else if(BigDecimal.class ==clazz){
                 BigDecimal bObj = (BigDecimal) obj;
                String val = bObj.toString();
                if(TypeUtil.isInteger(val)){
                    return new BigintType(Long.parseLong(val));
                }else if(TypeUtil.isDouble(val)){
                    return new DoubleType(Double.parseDouble(val));
                }else {
                    throw new BigSqlException(300 , "not support bigDecimal value : " + val);
                }
            }else{
                throw new BigSqlException(400 , "not support type : " + obj.getClass().getName());
            }
        }
    }

    public static Type[] getType(Object ... objs){
        if(objs.length == 0){
            return new Type[]{NullType.getInstance()};
        }else{
            Type[] types = new Type[objs.length];
            for(int i = 0 ; i < objs.length ; i ++){
                types[i] = getType(objs[i]);
            }
            return types;
        }
    }
}
