package com.xiaojiezhu.bigsql.sql.resolve.statement;

/**
 *
 * <pre>
 *     show databases;
 *     show tables;
 * </pre>
 *
 * time 2018/5/22 17:39
 *
 * @author xiaojie.zhu <br>
 */
public final class ShowComponentStatement extends CommandStatement{
    private Type type;

    public ShowComponentStatement(String sql) {
        super(sql);
        String[] split = sql.split(" ");
        type = Type.valueOf(split[1]);
    }



    public Type getShowType(){
        return type;
    }


    @Override
    public String toString() {
        return "ShowComponentStatement{" +
                "type=" + type +
                ", sql='" + sql + '\'' +
                '}';
    }

    /**
     * show type
     */
    public static enum Type{
        DATABASES("DATABASES"),
        TABLES("TABLES");

        private String value;

        Type(String value) {
            this.value = value;
        }



        @Override
        public String toString() {
            return value;
        }
    }
}
