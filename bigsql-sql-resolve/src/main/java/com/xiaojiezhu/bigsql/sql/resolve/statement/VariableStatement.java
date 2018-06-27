package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.xiaojiezhu.bigsql.common.LikeFieldUtil;
import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.model.construct.LikeField;

/**
 * 时间 2018/5/22 15:49
 * 说明 handler
 * <pre>
 *     show variables like 'xx'
 * </pre>
 *
 * @author xiaojie.zhu <br>
 */
public class VariableStatement implements Statement {
    public static final String FLAG = "'";
    protected String sql;

    protected LikeField likeField;

    private boolean init;

    public VariableStatement(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }


    public LikeField getLikeField() {
        synchronized (sql){
            if(!init){
                this.init = true;
                if(sql.contains(SqlConstant.LIKE)){
                    int firstIndex = sql.indexOf(FLAG);
                    int lastIndex = sql.indexOf(FLAG, firstIndex + 1);
                    String field = sql.substring(firstIndex + 1,lastIndex);
                    this.likeField = LikeFieldUtil.create(field);
                }

            }
            return likeField;
        }
    }


}
