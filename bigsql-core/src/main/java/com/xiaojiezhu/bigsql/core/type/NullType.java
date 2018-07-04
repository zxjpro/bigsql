package com.xiaojiezhu.bigsql.core.type;

/**
 * time 2018/7/4 16:54
 *
 * @author xiaojie.zhu <br>
 */
public class NullType implements Type {

    @Override
    public Object getValue() {
        return null;
    }


    @Override
    public String toString() {
        return null;
    }

    private NullType(){}
    private final static NullType INSTANCE = new NullType();

    public static NullType getInstance(){
        return INSTANCE;
    }
}
