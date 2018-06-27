package com.xiaojiezhu.bigsql.core.configuration;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.LikeField;
import com.xiaojiezhu.bigsql.model.construct.LikeMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * time 2018/5/22 16:45
 *
 * @author xiaojie.zhu <br>
 */
public class SimpleBigsqlVariable implements BigsqlVariable {
    private final Map<String,Entry> data;


    public SimpleBigsqlVariable(Map<String, Entry> data) {
        this.data = data;
    }

    @Override
    public List<String> getFields(LikeField likeField) {
        LikeMode likeMode = null;
        if(likeField != null){
            likeMode= likeField.getLikeMode();
        }

        List<String> tmp = new ArrayList<>();
        for (Map.Entry<String, Entry> entry : data.entrySet()) {
            String key = entry.getKey();
            if(LikeMode.LEFT.equals(likeMode)){
                if(key.endsWith(likeField.getField())){
                    tmp.add(key);
                }
            }else if(LikeMode.RIGHT.equals(likeMode)){
                if(key.startsWith(likeField.getField())){
                    tmp.add(key);
                }
            }else if(LikeMode.DOUBLE.equals(likeMode)){
                if(key.contains(likeField.getField())){
                    tmp.add(key);
                }
            }else if(likeMode == null){
                tmp.add(key);
            }else{
                throw new BigSqlException("like mode error , " + likeMode);
            }
        }
        return tmp;
    }

    @Override
    public Entry get(String key) {
        Entry entry = data.get(key);
        if(entry == null){
            throw new BigSqlException("not find variable : " + key);
        }
        return entry;
    }

}
