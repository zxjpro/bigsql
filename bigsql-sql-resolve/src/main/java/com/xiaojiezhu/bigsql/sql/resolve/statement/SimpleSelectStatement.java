package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.alibaba.druid.sql.SQLUtils;
import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SimpleField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SortField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * simple select,such as<br>
 *
 * <pre>
 * select now(),
 * select @@xxx
 * </pre>
 *
 * this class is must be final,because should use it to judge is a simple statement
 * <pre>
 *     if(statement instanceOf SimpleSelectStatement){
 *         //do something
 *     }
 * </pre>
 *
 * @author xiaojie.zhu
 */
public final class SimpleSelectStatement implements CommandSelectStatement {
    public final static Logger LOG = LoggerFactory.getLogger(SimpleSelectStatement.class);
    protected String sql;
    protected List<AliasField> queryField;

    public SimpleSelectStatement(String sql) {
        this.sql = sql;
    }

    /**
     * it is a lazy load,and not safe thread
     * @return
     */
    @Override
    public List<AliasField> getQueryField() {
        if(queryField == null){
            queryField = new ArrayList<>();
            String s = sql.replaceAll(SqlConstant.SELECT, "").trim();
            if(s.contains(SqlConstant.LIMIT)){
                s = s.substring(0,s.indexOf(SqlConstant.LIMIT));
            }
            String[] split = s.split(",");
            for (String f : split) {
                f = f.trim();
                String[] fieldGroup = f.split(SqlConstant.AS);
                if(fieldGroup.length == 1){
                    fieldGroup = f.split("\\s");
                }
                if(fieldGroup.length == 1){
                    this.queryField.add(new AliasField(fieldGroup[0].trim()));
                }else if(fieldGroup.length == 2){
                    this.queryField.add(new AliasField(fieldGroup[0].trim(),fieldGroup[1].trim()));
                }else{
                    LOG.warn("sql error , " + sql);
                }
            }
        }
        return queryField;
    }

    @Override
    public SimpleField getGroupField() {
        throw new BigSqlException(300 , "can not cal this method");
    }

    @Override
    public SortField getOrderField() {
        throw new BigSqlException(300 , "can not cal this method");
    }

    @Override
    public boolean isReadMaster() {
        throw new BigSqlException(300 , "can not cal this method");
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public String toString() {
        return sql;
    }

    public static SimpleSelectStatement parse(String sql)throws SqlParserException {
        if(sql.contains(SqlConstant.FROM)){
            throw new SqlParserException("the sql is not a simple select command , " + sql);
        }
        sql = SQLUtils.formatMySql(sql);
        SimpleSelectStatement ins = new SimpleSelectStatement(sql);
        return ins;
    }
}
