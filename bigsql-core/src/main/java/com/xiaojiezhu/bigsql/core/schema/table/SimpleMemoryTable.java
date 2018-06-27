package com.xiaojiezhu.bigsql.core.schema.table;

import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sharding.rule.Rule;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public class SimpleMemoryTable extends SimpleTable implements MemoryTable {
    private List<Field> fields;
    private ResultSet resultSet;

    public SimpleMemoryTable(String databaseName,String name, BigsqlContext context, Rule rule, List<Field> fields, ResultSet resultSet) {
        super(databaseName,name,context,rule);
        this.fields = fields;
        this.resultSet = resultSet;
    }

    @Override
    public Set<String> listColumnName() {
        Set<String> data = new HashSet<>(fields.size());
        for (Field field : fields) {
            data.add(field.getName());
        }
        return data;
    }

    @Override
    public List<Field> listColumnFields() {
        return fields;
    }

    @Override
    public ResultSet getResultSet() {
        return resultSet;
    }


}
