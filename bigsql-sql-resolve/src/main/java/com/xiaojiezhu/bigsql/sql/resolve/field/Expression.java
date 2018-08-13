package com.xiaojiezhu.bigsql.sql.resolve.field;

/**
 * the condition expression
 * @author zxj
 */
public class Expression{

    /**
     * > , >= , < , <=
     */
    private String expression;
    private Object value;

    public Expression() {
    }

    public Expression(String expression, Object value) {
        this.expression = expression;
        this.value = value;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return expression + " " + value;
    }

}