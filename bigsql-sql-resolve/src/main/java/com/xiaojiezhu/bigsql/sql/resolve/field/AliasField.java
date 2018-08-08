package com.xiaojiezhu.bigsql.sql.resolve.field;

import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.common.exception.SqlParserException;

/**
 * @author xiaojie.zhu
 */
public class AliasField implements Field{
    protected String name;
    protected String asName;
    protected FieldType fieldType;

    protected FunctionType functionType;


    public AliasField() {
    }

    public AliasField(String name) {
        this.setName(name);
        this.asName = name;
    }

    public AliasField(String name, String asName) {
        this(name);
        this.asName = asName;
    }

    public static enum FieldType{
        FIELD,
        FUNCTION,
        ENVIRONMENT,
    }

    public static enum FunctionType{
        COUNT,
        MAX,
        MIN,
        SUM,
        AVG
    }


    @Override
    public String getName() {
        return this.name;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public FunctionType getFunctionType(){
        return this.functionType;
    }



    public void setName(String name) {
        this.name = name;

        if(name.startsWith("@@")){
            this.fieldType = FieldType.ENVIRONMENT;
        }else if(name.matches("\\S+\\(\\S*\\)")){
            this.fieldType = FieldType.FUNCTION;

            if(name.startsWith(SqlConstant.COUNT)){
                this.functionType = FunctionType.COUNT;

            }else if(name.startsWith(SqlConstant.MAX)){
                this.functionType = FunctionType.MAX;

            }else if(name.startsWith(SqlConstant.MIN)){
                this.functionType = FunctionType.MIN;

            }else if(name.startsWith(SqlConstant.AVG)){
                this.functionType = FunctionType.AVG;

            }else if(name.startsWith(SqlConstant.SUM)){
                this.functionType = FunctionType.SUM;

            }else{
                throw new SqlParserException("not support function : " + name);
            }


        }else{
            this.fieldType = FieldType.FIELD;
        }
    }

    public String getAsName() {
        return asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
    }


    @Override
    public String toString() {
        return "AliasField{" +
                "name='" + name + '\'' +
                ", asName='" + asName + '\'' +
                ", fieldType=" + fieldType +
                '}';
    }
}
