package com.xiaojiezhu.bigsql.core.merge;

/**
 * time 2018/8/13 14:23
 *
 * @author xiaojie.zhu <br>
 */
public interface Reduce<T> {

    /**
     * reduce data
     * @param t1
     * @param t2
     * @return
     */
    T reduce(T t1, T t2);
}
