package com.xiaojiezhu.bigsql.sql.resolve.field;

import java.util.LinkedList;
import java.util.List;

/**
 * the field has a value ,such as
 * <pre>
 *      where id=1
 * </pre>
 * @author xiaojie.zhu
 */
public class ConditionField extends SimpleField {

    protected List<Expression> values = new LinkedList<>();

    public ConditionField(String name) {
        super(name);
    }


    public List<Expression> getValues() {
        return values;
    }

    public void setValues(List<Expression> values) {
        this.values = values;
    }


    public void addExpression(Expression expression){
        values.add(expression);
    }

    public void addExpression(List<Expression> expressions){
        values.addAll(expressions);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof ConditionField)){
            return false;
        }

        ConditionField that = (ConditionField) o;

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
