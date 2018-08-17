package com.xiaojiezhu.bigsql.core.configuration;

import com.xiaojiezhu.bigsql.common.Reloadable;
import com.xiaojiezhu.bigsql.model.construct.LikeField;

import java.util.List;

/**
 * time 2018/5/24 10:36
 *
 * @author xiaojie.zhu <br>
 */
public interface BigsqlConfiguration extends Reloadable {

    /**
     * select @@xxx
     * @param key @xxx
     * @return value
     */
    Entry getEnvironment(String key);

    /**
     * show variables
     * @param key variable
     * @return value
     */
    Entry getVariable(String key);

    /**
     * show variables like 'xx%'
     * @param likeField like mode
     * @return the like fields
     */
    List<String> getVariableLikeField(LikeField likeField);

    /**
     * get bigsql conf dir path
     * @return the dir of conf
     */
    String getConfDirPath();

    /**
     * when execute sharding sql , the concurrent number
     * @return
     */
    int getExecuteConcurrent();

    String getPassword();


}
