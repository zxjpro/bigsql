package com.xiaojiezhu.bigsql.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class SqlConstant {

    public static final String SELECT = "SELECT";
    public static final String LIKE = "LIKE";
    public static final String SHOW = "SHOW";
    public static final String OR = "OR";
    public static final String SHOW_DATABASES = "SHOW DATABASES";
    public static final String SHOW_VARIABLES = "SHOW VARIABLES";
    public static final String SHOW_TABLES = "SHOW TABLES";
    public static final String SHOW_STATUS = "SHOW STATUS";
    public static final String SHOW_CREATE_TABLE = "SHOW CREATE TABLE";
    public static final String SET = "SET";
    public static final String FROM = "FROM";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";
    public static final String INSERT = "INSERT";
    public static final String WHERE = "WHERE";
    public static final String AS = "AS";
    public static final String LIMIT = "LIMIT";
    public static final String VALUES = "VALUES";
    public static final String SHOW_ENGINES = "SHOW ENGINES";
    public static final String FIELD_ALOUD = "`";
    public static final String OPEN_TRANSACTION = "SET autocommit=0";
    public static final String COMMIT_TRANSACTION = "commit";
    public static final String ROLLBACK_TRANSACTION = "ROLLBACK";

    public static final String COUNT = "COUNT";
    public static final String MAX = "MAX";
    public static final String MIN = "MIN";
    public static final String SUM = "SUM";
    public static final String AVG = "AVG";


    public static final String DESC = "DESC";





    public static final List<String> MYSQL_DATABASE = new ArrayList<>();
    static {
        MYSQL_DATABASE.add("information_schema");
        MYSQL_DATABASE.add("mysql");
        MYSQL_DATABASE.add("performance_schema");
        MYSQL_DATABASE.add("sys");
    }
}
