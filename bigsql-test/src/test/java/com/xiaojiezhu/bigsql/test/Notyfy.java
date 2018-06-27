package com.xiaojiezhu.bigsql.test;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaojie.zhu
 */
public class Notyfy {
    private final Object lock = new Object();
    private int maxCount;
    private AtomicInteger count = new AtomicInteger(0);
    private EventLoopGroup group;
    private CountDownLatch cd;


    public Notyfy(int maxCount, EventLoopGroup group) {
        this.maxCount = maxCount;
        this.group = group;
    }

    public void add(String str){
        if(count.get() >= maxCount){
            //等有空余容量时，再添加进去
            if(cd != null){
                throw new RuntimeException("what gui");
            }
            cd = new CountDownLatch(1);
            try {
                cd.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        count.incrementAndGet();

        System.out.println("增加一个");
        group.execute(new Runnable() {
            @Override
            public void run() {
                System.err.println(Thread.currentThread().getName() + " \t " +  str);
                try {
                    Thread.sleep(1000 + new Random().nextInt(500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                synchronized (lock){
                    if(cd != null){
                        cd.countDown();
                        cd = null;
                    }
                    count.decrementAndGet();
                }
            }
        });
    }


    public static void main(String[] args) {
        Notyfy notyfy = new Notyfy(5, new NioEventLoopGroup(200));
        int i = 0 ;
        while (true){
            System.out.println("while on");
            notyfy.add("hello : " + (i++));
        }
    }


}
