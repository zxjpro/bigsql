package com.xiaojiezhu.bigsql.model.construct;

import com.xiaojiezhu.bigsql.model.constant.ColumnType;

/**
 * @author xiaojie.zhu
 */
public class Field {

    private String name = "";
    private boolean autoIncrement;
    private int nullable;
    private String asName = "";
    private int precision;
    private int scale;
    private String tableName = "";
    /**
     * the type as ,int,varchar,
     */
    private int fieldType;

    private boolean readOnly;
    private boolean writable;


    private String databaseName = "";
    private String fieldTypeName = "";

    private int columnDisplaySize;

    /**
     * create field
     * @param name fieldName
     * @param fieldLength field Length
     * @param columnType column type
     * @return
     */
    public static Field createField(String name,int fieldLength,ColumnType columnType){
        Field field = new Field();
        field.setAsName(name);
        field.setColumnDisplaySize(fieldLength);

        field.setName(name);
        field.setFieldType(columnType.getValue());
        field.setFieldTypeName(columnType.toString());
        return field;
    }



    public String getName() {
        return name;
    }

    public int getNullable() {
        return nullable;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public int isNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public String getAsName() {
        return asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public String getFieldTypeName() {
        return fieldTypeName;
    }

    public void setFieldTypeName(String fieldTypeName) {
        this.fieldTypeName = fieldTypeName;
    }

    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public void setColumnDisplaySize(int columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }
}
