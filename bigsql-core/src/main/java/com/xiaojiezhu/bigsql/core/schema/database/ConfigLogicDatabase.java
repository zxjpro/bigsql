package com.xiaojiezhu.bigsql.core.schema.database;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.DatabaseNotFoundException;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.schema.table.DefaultShardingTable;
import com.xiaojiezhu.bigsql.model.constant.Constant;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.DefaultShardingRule;

import java.io.File;
import java.util.HashMap;

/**
 * @author xiaojie.zhu
 */
public class ConfigLogicDatabase extends SimpleLogicDatabase {
    public static final String DATABASE_DEFAULT_CONFIG = "default.xml";
    protected BigsqlConfiguration configuration;
    protected BigsqlContext context;

    public ConfigLogicDatabase(String database, BigsqlContext context) {
        super(database);
        this.configuration = context.getBigsqlConfiguration();
        this.context = context;

        try {
            this.loadTables();
        } catch (Exception e) {
            throw new BigSqlException("reload table fail " ,e);
        }
    }

    private void loadTables() throws Exception {
        String databasePath = configuration.getConfDirPath() + Constant.SCHEMA_NAME + File.separator + database;
        File file = new File(databasePath);
        if(!file.exists()){
            throw new DatabaseNotFoundException("database : " + database + " config not found");
        }else{
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                this.tables = new HashMap<>((int)file.length());
                for (File tableConfig : files) {
                    if(tableConfig.isDirectory()){
                        throw new BigSqlException(100,"database config dir has a child dir");
                    }else{
                        if(DATABASE_DEFAULT_CONFIG.equals(tableConfig.getName())){
                            throw new BigSqlException("not complete code，default.xml");
                        }else{
                            String fileName = tableConfig.getName();
                            int index = fileName.indexOf(".");
                            String tableName = fileName.substring(0,index);


                            DefaultShardingRule shardingRule = DefaultShardingRule.create(tableConfig);
                            this.tables.put(tableName , new DefaultShardingTable(database,tableName,context,shardingRule));
                        }

                    }
                }

            }
        }
    }
}
