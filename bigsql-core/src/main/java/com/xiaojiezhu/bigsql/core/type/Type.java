package com.xiaojiezhu.bigsql.core.type;

/**
 * time 2018/7/4 16:23
 * the mysql column type
 * @author xiaojie.zhu <br>
 */
public interface Type<T> {

    /**
     * get the value
     * @return value
     */
    T getValue();

}
