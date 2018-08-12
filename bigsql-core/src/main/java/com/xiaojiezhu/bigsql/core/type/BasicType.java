package com.xiaojiezhu.bigsql.core.type;

/**
 * time 2018/7/4 16:26
 *
 * @author xiaojie.zhu <br>
 */
public class BasicType<T extends Comparable> implements Type<T> {

    protected T value;

    public BasicType(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    @Override
    public T getValue() {
        return this.value;
    }


    @Override
    public int compareTo(Type o) {
        return value.compareTo(o.getValue());
    }
}
