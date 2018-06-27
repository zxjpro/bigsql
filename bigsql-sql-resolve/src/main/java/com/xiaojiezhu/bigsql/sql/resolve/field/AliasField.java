package com.xiaojiezhu.bigsql.sql.resolve.field;

/**
 * @author xiaojie.zhu
 */
public class AliasField implements Field{
    protected String name;
    protected String asName;
    protected FieldType fieldType;


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

    @Override
    public String getName() {
        return this.name;
    }

    public FieldType getFieldType() {
        return fieldType;
    }



    public void setName(String name) {
        this.name = name;

        if(name.startsWith("@@")){
            this.fieldType = FieldType.ENVIRONMENT;
        }else if(name.matches("\\S+\\(\\S*\\)")){
            this.fieldType = FieldType.FUNCTION;
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
