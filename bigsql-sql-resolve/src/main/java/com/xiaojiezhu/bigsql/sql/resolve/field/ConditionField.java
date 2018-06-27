package com.xiaojiezhu.bigsql.sql.resolve.field;

import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class ConditionField extends ValueField {
    protected String operator;

    public ConditionField(String name, String operator) {
        super(name);
        this.operator = operator;
    }

    public ConditionField(String name, List<Object> values, String operator) {
        super(name, values);
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConditionField)) return false;
        if (!super.equals(o)) return false;

        ConditionField that = (ConditionField) o;

        if (operator != null ? !operator.equals(that.operator) : that.operator != null) return false;
        if (values != null ? !values.equals(that.values) : that.values != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + operator + values;
    }
}
