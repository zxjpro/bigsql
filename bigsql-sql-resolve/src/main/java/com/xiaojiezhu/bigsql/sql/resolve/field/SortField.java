package com.xiaojiezhu.bigsql.sql.resolve.field;

/**
 * @author xiaojie.zhu
 */
public class SortField implements Field {
    protected String name;

    /**
     * true asc,
     * false desc
     */
    protected boolean asc;

    public SortField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }


    @Override
    public String toString() {
        String t = asc ? "asc" : "desc";
        return name + " " + t;

    }
}
