package com.xiaojiezhu.bigsql.core;

import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.core.type.TypeFactory;
import com.xiaojiezhu.bigsql.model.construct.Field;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class ResultSetUtil {

    public static List<Type[]> getRowData(ResultSet resultSet) throws SQLException {
        List<Type[]> rowData = new ArrayList<>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()){
            Type[] data = new Type[columnCount];
            for(int i = 1 ; i <= columnCount ; i ++){
                data[i - 1] = TypeFactory.getType(resultSet.getObject(i));
            }
            rowData.add(data);
        }
        return rowData;
    }

    public static List<Type[]> getRowData(List<ResultSet> resultSets) throws SQLException{
        List<Type[]> rows = new LinkedList<>();
        for (ResultSet resultSet : resultSets) {
            List<Type[]> rowData = getRowData(resultSet);
            rows.addAll(rowData);
        }
        return rows;
    }

    public static List<Field> getFields(String databaseName,String tableName,ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Field> fields = new LinkedList<>();
        for(int i = 1 ; i <= columnCount ; i ++){
            Field field = new Field();
            field.setAsName(metaData.getColumnName(i));
            field.setAutoIncrement(metaData.isAutoIncrement(i));
            field.setColumnDisplaySize(metaData.getColumnDisplaySize(i));
            field.setDatabaseName(databaseName);
            field.setFieldType(metaData.getColumnType(i));
            field.setFieldTypeName(metaData.getColumnTypeName(i));
            field.setName(metaData.getColumnName(i));
            field.setNullable(metaData.isNullable(i));
            field.setPrecision(metaData.getPrecision(i));
            field.setReadOnly(metaData.isReadOnly(i));
            field.setScale(metaData.getScale(i));
            field.setTableName(tableName);
            field.setWritable(metaData.isWritable(i));
            fields.add(field);
        }
        return fields;
    }


}
