package com.xiaojiezhu.bigsql.sql.resolve.field;

/**
 * @author xiaojie.zhu
 */
public class SimpleField implements Field{
    protected String name;

    public SimpleField(String name) {
        this.name = name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}
