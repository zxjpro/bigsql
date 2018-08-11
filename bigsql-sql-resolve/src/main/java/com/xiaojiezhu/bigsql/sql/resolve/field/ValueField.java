package com.xiaojiezhu.bigsql.sql.resolve.field;

/**
 * the field has a value ,such as
 * insert field and value
 * @author xiaojie.zhu
 */
public class ValueField extends SimpleField {

    protected Object value;

    public ValueField(String name) {
        super(name);
    }

    public ValueField(String name, Object value) {
        super(name);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof ValueField)){
            return false;
        }

        ValueField that = (ValueField) o;

        if (value != null ? !value.equals(that.value) : that.value != null){
            return false;
        }
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + " , " + value;
    }
}
