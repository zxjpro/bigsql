package com.xiaojiezhu.bigsql.core.type;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;

/**
 * time 2018/7/4 16:39
 *
 * @author xiaojie.zhu <br>
 */
public class TinyintType extends BasicType<Integer> {

    public TinyintType(Integer value) {
        super(value);
    }

    public TinyintType(boolean value) {
        super(value ? 1 : 0);
    }



    /**
     * get the boolean of tinyint
     * @return
     */
    public boolean getBoolean(){
        if(value != 1 && value != 0){
            throw new BigSqlException(300 , "boolean tinyint just be 0 and 1 ,this is :" + value);
        }
        return value == 1;
    }
}
