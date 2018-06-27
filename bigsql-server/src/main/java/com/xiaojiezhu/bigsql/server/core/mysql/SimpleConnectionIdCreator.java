package com.xiaojiezhu.bigsql.server.core.mysql;

/**
 * @author xiaojie.zhu
 */
public class SimpleConnectionIdCreator implements ConnectionIdCreator{

    private SimpleConnectionIdCreator(){}

    private int value;



    public static SimpleConnectionIdCreator getInstance(){
        return Instance.INSTANCE;
    }

    @Override
    public synchronized int get() {
        if(value == Integer.MAX_VALUE){
            value = 0;
        }
        return ++value;
    }

    private static class Instance{
        private static final SimpleConnectionIdCreator INSTANCE = new SimpleConnectionIdCreator();
    }
}
