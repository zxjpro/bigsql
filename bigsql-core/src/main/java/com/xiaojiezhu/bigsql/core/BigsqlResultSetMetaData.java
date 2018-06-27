package com.xiaojiezhu.bigsql.core;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.util.Asserts;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class BigsqlResultSetMetaData implements ResultSetMetaData {
    public static final RuntimeException E = new RuntimeException("can not call this method");
    private List<Field> fields;

    private BigsqlResultSetMetaData() {
    }

    public static BigsqlResultSetMetaData createEmptyInstance(){
        return new BigsqlResultSetMetaData();
    }

    public static BigsqlResultSetMetaData createInstance(List<Field> fields){
        Asserts.collectionIsNotNull(fields,"fields can not be empty");
        BigsqlResultSetMetaData metaData = new BigsqlResultSetMetaData();
        metaData.fields = fields;
        return metaData;
    }

    @Override
    public int getColumnCount() throws SQLException {
        if(fields == null){
            return 0;
        }else{
            return fields.size();
        }
    }

    private Field getField(int column){
        try {
            Field field = fields.get(column - 1);
            if(field == null){
                throw new BigSqlException("the column:" + column + " can not be null");
            }
            return field;
        } catch (Exception e) {
            throw new IndexOutOfBoundsException("the column:" + column + " is out of bounds");
        }
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        Field field = getField(column);
        return field.isAutoIncrement();
    }


    @Override
    public int isNullable(int column) throws SQLException {
        Field field = getField(column);
        return field.isNullable();
    }

    /**
     * this is a alias name,
     *
     * name_ as 'username',
     *
     * if it not exists,then use the real name
     * @param column
     * @return
     * @throws SQLException
     */
    @Override
    public String getColumnLabel(int column) throws SQLException {
        Field field = getField(column);
        return field.getAsName();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        Field field = getField(column);
        return field.getAsName();
    }
    @Override
    public int getPrecision(int column) throws SQLException {
        Field field = getField(column);
        return field.getPrecision();
    }

    @Override
    public int getScale(int column) throws SQLException {
        Field field = getField(column);
        return field.getScale();
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return getField(column).getTableName();
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        Field field = getField(column);
        return field.getFieldType();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        Field field = getField(column);
        return field.isReadOnly();
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        Field field = getField(column);
        return field.isWritable();
    }
    @Override
    public String getCatalogName(int column) throws SQLException {
        return getField(column).getDatabaseName();
    }



    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return getField(column).getFieldTypeName();
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        //这个好像是字段长度，比如指定的varchar的长度
        return getField(column).getColumnDisplaySize();
    }







    //=======================================can not use===========================================================


    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        throw E;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        throw E;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        throw E;
    }


    @Override
    public boolean isSigned(int column) throws SQLException {
        throw E;
    }



    @Override
    public String getSchemaName(int column) throws SQLException {
        throw E;
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        throw E;
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        // 这里后面可以根据情况做一个根据值，获取java类型的名字
        throw E;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw E;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw E;
    }
}
