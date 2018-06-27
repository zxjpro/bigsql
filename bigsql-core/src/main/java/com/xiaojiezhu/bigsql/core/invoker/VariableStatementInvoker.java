package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.configuration.Entry;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.model.construct.LikeField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.VariableStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * response a resultSet, handler something simple query
 *
 * <pre>
 *     show variables like 'xx%'
 * </pre>
 * @author xiaojie.zhu
 */
public class VariableStatementInvoker extends AbstractKeyValueQueryStatementInvoker {
    public static final Logger LOG = LoggerFactory.getLogger(VariableStatementInvoker.class);
    public static final String VARIABLE_NAME = "Variable_name";
    public static final String VARIABLE_VALUE = "Value";
    public static final int COLUMN_MAX_LENGTH = 64;

    private List<String> keys;


    public VariableStatementInvoker(Statement statement, BigsqlConfiguration configuration) {
        super(statement,configuration);
    }

    @Override
    protected List<Field> getQueryFields() {
        Field keyField = createField(VARIABLE_NAME);
        Field valueField = createField(VARIABLE_VALUE);
        return Arrays.asList(keyField,valueField);

    }

    private Field createField(String fieldName) {
        Field field = Field.createField(fieldName, COLUMN_MAX_LENGTH, ColumnType.VARCHAR);
        return field;
    }


    @Override
    protected List<String> getKeys() {
        if(this.keys == null){
            if(statement instanceof VariableStatement){
                VariableStatement variableStatement = (VariableStatement) statement;
                LikeField likeField = variableStatement.getLikeField();
                List<String> fields = configuration.getVariableLikeField(likeField);
                if(fields == null || fields.size() == 0){
                    throw new BigSqlException("get variables error,it can not be empty , sql:" + statement.getSql());
                }else{
                    this.keys = new ArrayList<>(fields.size());
                    this.keys.addAll(fields);
                }
            }else{
                throwNotSupport();
            }
        }
        return this.keys;
    }

    @Override
    protected Entry getValue(String fieldName) {
        return configuration.getVariable(fieldName);
    }

}
