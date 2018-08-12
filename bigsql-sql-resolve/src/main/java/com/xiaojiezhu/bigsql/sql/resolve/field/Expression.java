package com.xiaojiezhu.bigsql.sql.resolve.field;

/**
 * the condition expression
 * @author zxj
 */
public class Expression{
    public static final String GREAT = ">";
    public static final String GREAT_EQUALS = ">=";
    public static final String LESS = "<";
    public static final String LESS_EQUALS = "<=";
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