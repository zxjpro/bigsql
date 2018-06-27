package com.xiaojiezhu.bigsql.core.configuration;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/7 15:04
 * 说明 bigsql environment
 */
class SimpleBigsqlEnvironment implements BigsqlEnvironment {
    private final Map<String,Entry> data;

    public SimpleBigsqlEnvironment(Map<String, Entry> data) {
        this.data = data;
    }

    @Override
    public Entry get(String key) {
        Entry entry = data.get(key);
        if(entry == null){
            throw new BigSqlException("not find env : " + key);
        }
        return entry;
    }





}
