package com.xiaojiezhu.bigsql.core.merge.fn;

import com.xiaojiezhu.bigsql.common.exception.MergeException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.ResultSetUtil;
import com.xiaojiezhu.bigsql.core.merge.Merge;
import com.xiaojiezhu.bigsql.core.merge.Reduce;
import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.core.type.TypeFactory;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * time 2018/8/13 14:27
 *
 * @author xiaojie.zhu <br>
 */
public abstract class AbstractFnMerge implements Merge, Reduce<Object> {
    public final static Logger LOG = LoggerFactory.getLogger(AbstractFnMerge.class);

    private String databaseName;
    private String tableName;
    private List<ResultSet> resultSets;


    public AbstractFnMerge(String databaseName, String tableName, List<ResultSet> resultSets) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.resultSets = resultSets;
    }

    @Override
    public ResultSet merge() throws MergeException {
        List<Field> fields = getFields();
        Object tmp = null;
        for (ResultSet resultSet : resultSets) {
            try {
                while (resultSet.next()){
                    Object val = resultSet.getObject(1);
                    if(tmp == null){
                        tmp = val;
                    }else if(val == null){
                        //nothing to do
                    }else{
                        tmp = this.reduce(tmp,val);
                    }


                }
            } catch (SQLException e) {
                LOG.error("merge resultSet error , resultSet:" + resultSet , e);
                throw new MergeException(e.getErrorCode(),e.getMessage(),e);
            }
        }

        List<Type[]> rowData = new ArrayList<>(1);
        rowData.add(new Type[]{TypeFactory.getType(tmp)});

        BigsqlResultSet resultSet = BigsqlResultSet.createInstance(fields, rowData);
        return resultSet;
    }


    /**
     * get the resultSet fields
     * @return
     */
    private List<Field> getFields() {
        try {
            List<Field> fields = ResultSetUtil.getFields(databaseName, tableName, resultSets.get(0));
            return fields;
        } catch (SQLException e) {
            LOG.error("merge resultSet error",e);
            throw new MergeException("merge resultSet error" , e);
        }
    }
}
