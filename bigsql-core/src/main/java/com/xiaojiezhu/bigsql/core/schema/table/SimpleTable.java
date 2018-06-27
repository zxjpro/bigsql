package com.xiaojiezhu.bigsql.core.schema.table;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.sharding.Strategy;
import com.xiaojiezhu.bigsql.sharding.rule.Rule;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import com.xiaojiezhu.bigsql.util.BeanUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public abstract class SimpleTable implements LogicTable {
    protected String name;
    protected String databaseName;
    protected BigsqlContext context;
    protected Rule rule;

    public SimpleTable(String databaseName,String name,BigsqlContext context,Rule rule) {
        this.databaseName = databaseName;
        this.name = name;
        this.context = context;
        this.rule = rule;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }


    private Object getClassTypeObject(Set<Object> injectParam,Class<?> clazz){
        for (Object obj : injectParam) {
            if(obj.getClass() == clazz){
                return obj;
            }else {
                if(clazz.isAssignableFrom(obj.getClass())){
                    return obj;
                }
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "table : " + name;
    }


    /**
     * the sub class can over
     * @return
     */
    protected Set<Object> injectParam(){
        return null;
    }

    /**
     * create the strategy
     * @param statement
     * @return
     */
    protected Strategy createStrategy(Statement statement){
        Set<Object> injectParam = new HashSet<>();
        injectParam.add(statement);
        injectParam.add(rule);
        injectParam.add(context.getDataSourcePool());

        Set<Object> objects = injectParam();
        if(!BeanUtil.isEmpty(objects)){
            injectParam.addAll(objects);
        }

        Class<? extends Strategy> strategyClass = null;
        try {
            strategyClass = context.getStrategyPool().getStrategy(rule.getStrategyName());
        } catch (Exception e){
            throw new BigSqlException(100 , "strategy load fail , " + rule.getStrategyName() , e);
        }
        Constructor<?> constructor = strategyClass.getConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0 ; i < parameterTypes.length ; i ++) {
            Class<?> parameterType = parameterTypes[i];
            Object object = getClassTypeObject(injectParam, parameterType);
            if(object == null){
                throw new BigSqlException(300,"database:"+databaseName+" , table:"+name+" create strategy fail , use strategy name:"+rule.getStrategyName()+" , " +
                        "strategy class name : "+strategyClass.getName()+" , not found parameter:" + parameterType);
            }else{
                params[i] = object;
            }
        }

        try {
            Object instance = constructor.newInstance(params);
            return (Strategy) instance;
        } catch (Exception e) {
            throw new BigSqlException(300,"database:"+databaseName+" , table:"+name+" create strategy fail , use strategy name:"+rule.getStrategyName()+" , " +
                    "strategy class name : "+strategyClass.getName() , e);
        }
    }


}
