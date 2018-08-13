package com.xiaojiezhu.bigsql.core.merge.fn;

import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.sql.ResultSet;
import java.util.List;

/**
 * time 2018/8/13 14:40
 *
 * @author xiaojie.zhu <br>
 */
public class MinMerge extends AbstractFnMerge {
    public MinMerge(String databaseName, String tableName, List<ResultSet> resultSets) {
        super(databaseName, tableName, resultSets);
    }

    @Override
    public Object reduce(Object t1, Object t2) {
        if(TypeUtil.isInteger(t2)){
            long l1 = Long.parseLong(String.valueOf(t1));
            long l2 = Long.parseLong(String.valueOf(t2));
            Long min = Long.min(l1,l2);
            return min;
        }else{
            double d1 = Double.parseDouble(String.valueOf(t1));
            double d2 = Double.parseDouble(String.valueOf(t2));

            Double min = Double.min(d1,d2);
            return min;
        }
    }


}
