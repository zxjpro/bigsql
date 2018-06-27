package com.xiaojiezhu.bigsql.core.merge;


import com.xiaojiezhu.bigsql.common.exception.MergeException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.ResultSetUtil;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * each item add merge
 * @author xiaojie.zhu
 */
public class ItemMerge implements Merge {
    public final static Logger LOG = LoggerFactory.getLogger(ItemMerge.class);
    private List<ResultSet> resultSets;
    private String databaseName;
    private String tableName;

    public ItemMerge( String databaseName, String tableName,List<ResultSet> resultSets) {
        Asserts.collectionIsNotNull(resultSets,"ResultSet can not be null");
        this.resultSets = resultSets;
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    @Override
    public ResultSet merge() throws MergeException {
        try {
            List<Field> fields = ResultSetUtil.getFields(databaseName, tableName, resultSets.get(0));
            List<Object[]> rowData = ResultSetUtil.getRowData(resultSets);
            BigsqlResultSet resultSet = BigsqlResultSet.createInstance(fields, rowData);
            return resultSet;
        } catch (SQLException e) {
            throw new MergeException("merge resultSet error : " + e.getMessage() , e);
        }
    }
}
