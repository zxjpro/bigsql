package com.xiaojiezhu.bigsql.core;


import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.DefaultCommandSelectStatement;
import com.xiaojiezhu.bigsql.util.Asserts;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author xiaojie.zhu
 */
public class BigsqlResultSet implements ResultSet {
    public static final RuntimeException E = new RuntimeException("can not call this method");
    private List<Field> fields;
    private BigsqlResultSetMetaData metaData;
    private List<Type[]> rowData;
    private Iterator<Type[]> iterator;
    private Type[] current;
    private Map<String,Integer> mapping = new HashMap<>();

    /**
     * when not call next() method,it is true
     */
    private boolean beforeFirst = true;

    private String warnings = "";

    private BigsqlResultSet() {
    }

    public static BigsqlResultSet createInstance(List<Field> fields,List<Type[]> rowData){
        BigsqlResultSet resultSet = new BigsqlResultSet();
        resultSet.fields = fields;
        resultSet.metaData = BigsqlResultSetMetaData.createInstance(fields);
        resultSet.setRowData(rowData);
        return resultSet;
    }

    /**
     * create resultSet , the resultSet has column ,but row data is null
     * @param selectStatement statement
     * @return result set
     */
    public static BigsqlResultSet createNullResultSet(DefaultCommandSelectStatement selectStatement) {
        List<AliasField> queryField = selectStatement.getQueryField();
        List<Field> fields = new ArrayList<>(queryField.size());
        for (AliasField aliasField : queryField) {
            fields.add(Field.createField(aliasField.getAsName(),64,ColumnType.VARCHAR));
        }
        List<Type[]> rowData = new ArrayList<>(1);
        return createInstance(fields,rowData);
    }


    private void setRowData(List<Type[]> rowData){
        this.rowData = rowData;
        if(rowData != null){
            this.iterator = this.rowData.iterator();
        }
    }

    @Override
    public boolean next() throws SQLException {
        if(iterator == null){
            return false;
        }
        boolean b = iterator.hasNext();
        if(b){
            this.current = iterator.next();
            this.beforeFirst = true;
        }
        return b;
    }

    private Object findObject(int columnIndex){
        if(current == null){
            throw new BigSqlException("the current data is null");
        }
        try {
            Type type = current[columnIndex - 1];
            return type.getValue();
        } catch (Exception e) {
            throw new IndexOutOfBoundsException("the columnIndex:" + columnIndex + " is out of " + current.length);
        }
    }

    /**
     * find object by field name
     * @param fieldAsName
     * @return
     */
    private Object findObject(String fieldAsName){
        Asserts.notEmpty(fieldAsName,"fieldName can not be null");
        Integer index = mapping.get(fieldAsName);
        if(index == null){
            index = cacheMapping(fieldAsName);
        }
        if(index == null){
            throw new SqlParserException("not find field :　" + fieldAsName);
        }

        Type<?> type = current[index - 1];
        return type.getValue();

    }

    private Integer cacheMapping(String fieldAsName) {
        Integer index = null;
        for(int i = 0 ; i < fields.size() ; i ++){
            if(fieldAsName.equals(fields.get(i).getAsName())){
                index = i + 1;
                mapping.put(fieldAsName,index);
                break;
            }
        }
        return index;
    }


    @Override
    public String getString(int columnIndex) throws SQLException {
        return String.valueOf(findObject(columnIndex));
    }





    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return (boolean) findObject(columnIndex);
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return (byte) findObject(columnIndex);
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        return (short) findObject(columnIndex);
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        return (int) findObject(columnIndex);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        return (long) findObject(columnIndex);
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        return (float) findObject(columnIndex);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        return (double) findObject(columnIndex);
    }


    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return (byte[]) findObject(columnIndex);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return (Date) findObject(columnIndex);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return (Time) findObject(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return (Timestamp) findObject(columnIndex);
    }



    @Override
    public String getString(String columnLabel) throws SQLException {
        return (String) findObject(columnLabel);
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        return (boolean) findObject(columnLabel);
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        return (byte) findObject(columnLabel);
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        return (short) findObject(columnLabel);
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return (int) findObject(columnLabel);
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        return (long) findObject(columnLabel);
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        return (float) findObject(columnLabel);
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        return (double) findObject(columnLabel);
    }


    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return (byte[]) findObject(columnLabel);
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return (Date) findObject(columnLabel);
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return (Time) findObject(columnLabel);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return (Timestamp) findObject(columnLabel);
    }


    @Override
    public SQLWarning getWarnings() throws SQLException {
        return new SQLWarning(this.warnings);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return metaData;
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return findObject(columnIndex);
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return findObject(columnLabel);
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        //index from 1
        Integer index = mapping.get(columnLabel);
        if(index == null){
            index = cacheMapping(columnLabel);
        }
        if(index == null){
            throw new SqlParserException("can fond columnLabel:" + columnLabel);
        }
        return index;
    }


    @Override
    public boolean isBeforeFirst() throws SQLException {
        return beforeFirst;
    }









    //=========================下方方法不可用===============================




    @Override
    public void close() throws SQLException {
        throw E;
    }

    @Override
    public boolean wasNull() throws SQLException {
        throw E;
    }


    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        throw E;
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        throw E;
    }
    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        throw E;
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        throw E;
    }
    @Override
    public void clearWarnings() throws SQLException {
        throw E;
    }


    @Override
    public String getCursorName() throws SQLException {
        throw E;
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        throw E;
    }
    @Override
    public boolean isAfterLast() throws SQLException {
        throw E;
    }

    @Override
    public boolean isFirst() throws SQLException {
        throw E;
    }

    @Override
    public boolean isLast() throws SQLException {
        throw E;
    }

    @Override
    public void beforeFirst() throws SQLException {
        throw E;
    }

    @Override
    public void afterLast() throws SQLException {
        throw E;
    }

    @Override
    public boolean first() throws SQLException {
        throw E;
    }

    @Override
    public boolean last() throws SQLException {
        throw E;
    }

    @Override
    public int getRow() throws SQLException {
        throw E;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        throw E;
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        throw E;
    }

    @Override
    public boolean previous() throws SQLException {
        //与next方法相反，后面考虑是否支持
        throw E;

    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        throw E;
    }
    @Override
    public int getFetchDirection() throws SQLException {
        throw E;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        throw E;
    }

    @Override
    public int getFetchSize() throws SQLException {
        throw E;
    }
    @Override
    public int getType() throws SQLException {
        throw E;
    }

    @Override
    public int getConcurrency() throws SQLException {
        throw E;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        throw E;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        throw E;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        throw E;
    }
    @Override
    public void updateNull(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw E;
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw E;
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw E;
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw E;
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw E;
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw E;
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw E;
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw E;
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        throw E;
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw E;
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw E;
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw E;
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw E;
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw E;
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw E;
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw E;
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw E;
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw E;
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw E;
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw E;
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw E;
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw E;
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw E;
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw E;
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw E;
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw E;
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw E;
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw E;
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw E;
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw E;
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw E;
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw E;
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw E;
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw E;
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw E;
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw E;
    }
    @Override
    public void insertRow() throws SQLException {
        throw E;
    }

    @Override
    public void updateRow() throws SQLException {
        throw E;
    }

    @Override
    public void deleteRow() throws SQLException {
        throw E;
    }

    @Override
    public void refreshRow() throws SQLException {
        throw E;
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw E;
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw E;
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw E;
    }
    @Override
    public Statement getStatement() throws SQLException {
        throw E;
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        throw E;
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        throw E;
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw E;
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        throw E;
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw E;
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        throw E;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        throw E;
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        throw E;
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw E;
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw E;
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw E;
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw E;
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw E;
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw E;
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw E;
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw E;
    }
    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw E;
    }



    @Override
    public int getHoldability() throws SQLException {
        throw E;
    }

    @Override
    public boolean isClosed() throws SQLException {
        throw E;
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw E;
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw E;
    }
    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw E;
    }



    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw E;
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw E;
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw E;
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw E;
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw E;
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw E;
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw E;
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw E;
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw E;
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw E;
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw E;
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw E;
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw E;
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw E;
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw E;
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw E;
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw E;
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw E;
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw E;
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw E;
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw E;
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        throw E;
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
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
