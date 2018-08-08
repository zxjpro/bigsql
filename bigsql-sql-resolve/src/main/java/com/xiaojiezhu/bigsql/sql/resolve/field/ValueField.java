package com.xiaojiezhu.bigsql.sql.resolve.field;

import java.util.List;

/**
 * the field has a values ,such as
 * <pre>
 *      where id=1
 * </pre>
 * @author xiaojie.zhu
 */
public class ValueField extends SimpleField {

    protected List<Object> values;

    public ValueField(String name) {
        super(name);
    }

    public ValueField(String name, List<Object> values) {
        super(name);
        this.values = values;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
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

        if (values != null ? !values.equals(that.values) : that.values != null){
            return false;
        }
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = values != null ? values.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + "=" + values;
    }
}
