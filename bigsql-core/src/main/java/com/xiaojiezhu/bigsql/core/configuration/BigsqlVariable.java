package com.xiaojiezhu.bigsql.core.configuration;


import com.xiaojiezhu.bigsql.model.construct.LikeField;

import java.util.List;

/**
 * time 2018/5/22 16:42
 *
 * @author xiaojie.zhu <br>
 */
public interface BigsqlVariable extends BigsqlEnvironment{

    /**
     * get the variables name
     * @param likeField like field
     * @return return a list of variable name
     */
    List<String> getFields(LikeField likeField);

}
