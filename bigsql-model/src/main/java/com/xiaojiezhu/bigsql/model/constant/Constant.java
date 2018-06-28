package com.xiaojiezhu.bigsql.model.constant;

/**
 * @author xiaojie.zhu
 */
public class Constant {

    public static final int MYSQL_PROTOCOL_VERSION = 0x0A;


    public static final String SERVER_INFO = "5.6.35-bigsql";
    //public static final String SERVER_INFO = "bigsql-1.0";

    /**
     * 0x21 is utf8_general_ci
     *
     * see https://dev.mysql.com/doc/internals/en/character-set.html
     */
    public static final int CHARSET = 0x21;

    public static final int NULL = 0x00;

    /**
     * result row data,null byte,
     * https://dev.mysql.com/doc/internals/en/com-query-response.html#text-resultset
     */
    public static final int ROW_NULL = 0xfb;

    public static final int _250 = 250;
    public static final int _251 = 251;
    public static final int _252 = 252;
    public static final int _253 = 253;
    public static final int _254 = 254;
    public static final int _2 = 2;
    public static final int _16 = 16;
    public static final int _24 = 24;
    public static final int _64 = 64;

    public static final String SQL_TYPE = "mysql";


    public static final String DEFAULT_DATABASE_NAME = "bigsql";

    public static final String SCHEMA_NAME = "schema";

    public static final String ENGINES_NAME = "ENGINES";
    public static final String BIGSQL_DEV = "bigsql.dev";





}
