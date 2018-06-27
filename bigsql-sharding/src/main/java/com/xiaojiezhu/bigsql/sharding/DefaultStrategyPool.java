package com.xiaojiezhu.bigsql.sharding;

import com.xiaojiezhu.bigsql.common.ClassContext;
import com.xiaojiezhu.bigsql.common.annotation.EnableStrategy;

import java.util.Set;

/**
 * time 2018/6/25 17:20
 *
 * @author xiaojie.zhu <br>
 */
public class DefaultStrategyPool implements StrategyPool {
    private ClassContext classContext;
    private Set<Class<?>> classes;


    private DefaultStrategyPool(ClassContext classContext) {
        this.classContext = classContext;
    }

    @Override
    public Class<? extends Strategy> getStrategy(String name) {
        if(this.classes == null){
            return null;
        }else{
            for (Class<?> strategy : classes) {
                if(strategy.getSimpleName().equals(name)){
                    return (Class<? extends Strategy>) strategy;
                }
            }
            return null;
        }
    }



    @Override
    public void reload() throws Exception {
        classContext.reload();
        Set<Class<?>> tmp =  classContext.getByAnnotation(EnableStrategy.class);
        this.classes = tmp;
    }


    public static DefaultStrategyPool newInstance(ClassContext classContext) throws Exception {
        DefaultStrategyPool pool = new DefaultStrategyPool(classContext);
        pool.reload();
        return pool;
    }
}
