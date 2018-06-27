package com.xiaojiezhu.bigsql.common;

import com.xiaojiezhu.bigsql.model.construct.LikeField;
import com.xiaojiezhu.bigsql.model.construct.LikeMode;
import com.xiaojiezhu.bigsql.util.Asserts;

/**
 * @author xiaojie.zhu
 */
public class LikeFieldUtil {
    public static final String FLAG = "%";


    public static LikeField create(String field){
        Asserts.notEmpty(field,"field can not be empty");
        if(field.startsWith(FLAG) && field.endsWith(FLAG)){
            String f = field.substring(1, field.length() - 1);
            return new LikeField(field,f, LikeMode.DOUBLE);
        }else if(field.endsWith(FLAG)){
            String f = field.substring(0, field.length() - 1);
            return new LikeField(field,f,LikeMode.RIGHT);
        }else{
            String f = field.substring(1);
            return new LikeField(field,f,LikeMode.LEFT);
        }
    }

}
