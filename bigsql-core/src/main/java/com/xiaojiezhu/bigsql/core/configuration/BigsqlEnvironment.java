package com.xiaojiezhu.bigsql.core.configuration;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/7 14:59
 * 说明 environment
 */
public interface BigsqlEnvironment {

    /**
     * get value
     * @param key
     * @return
     */
    Entry get(String key);


}
