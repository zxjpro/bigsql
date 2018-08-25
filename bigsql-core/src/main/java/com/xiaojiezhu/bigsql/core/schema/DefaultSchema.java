package com.xiaojiezhu.bigsql.core.schema;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.DatabaseNotFoundException;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.schema.database.ConfigLogicDatabase;
import com.xiaojiezhu.bigsql.core.schema.database.LogicDatabase;
import com.xiaojiezhu.bigsql.core.schema.database.MasterSlaveDatabase;
import com.xiaojiezhu.bigsql.core.schema.database.SimpleLogicDatabase;
import com.xiaojiezhu.bigsql.core.schema.table.LogicTable;
import com.xiaojiezhu.bigsql.core.schema.table.bigsql.ConnectionPoolTable;
import com.xiaojiezhu.bigsql.core.schema.table.bigsql.ConnectionTable;
import com.xiaojiezhu.bigsql.model.constant.Constant;
import com.xiaojiezhu.bigsql.sharding.rule.masterslave.DefaultMasterSlaveRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

import static com.xiaojiezhu.bigsql.model.constant.Constant.DEFAULT_DATABASE_NAME;

/**
 * @author xiaojie.zhu
 */
public class DefaultSchema implements Schema {
    public final static Logger LOG = LoggerFactory.getLogger(DefaultSchema.class);

    public static final String DEFAULT_XML = "default.xml";

    private final BigsqlConfiguration configuration;

    private Set<String> database;

    private Map<String,LogicDatabase> databaseMap;
    protected final BigsqlContext context;


    public DefaultSchema(BigsqlContext context) {
        this.configuration = context.getBigsqlConfiguration();
        this.context = context;
        this.reload();
    }

    @Override
    public Set<String> listDatabaseName() {
        return database;
    }

    @Override
    public LogicDatabase getDatabase(String database)throws DatabaseNotFoundException {
        LogicDatabase logicDatabase = databaseMap.get(database);
        if(logicDatabase == null){
            throw new DatabaseNotFoundException("database : " + database + " not found");
        }
        return logicDatabase;
    }


    @Override
    public void reload() {
        String schemaPath = configuration.getConfDirPath() + Constant.SCHEMA_NAME;
        //init database name
        initDatabase(schemaPath);

        Map<String,LogicDatabase> databaseMap = new HashMap<>(database.size());
        //add bigsql database first
        databaseMap.put(Constant.DEFAULT_DATABASE_NAME,getBigsqlDatabase());
        for (String databaseName : database) {
            if(!Constant.DEFAULT_DATABASE_NAME.equals(databaseName)){
                String tablePath = schemaPath + File.separator + databaseName;
                File defaultXmlPath = new File(tablePath + File.separator + DEFAULT_XML);
                if(!defaultXmlPath.exists()){
                    databaseMap.put(databaseName,new ConfigLogicDatabase(databaseName,context));
                }else{
                    //has default.xml
                    try {
                        databaseMap.put(databaseName , new MasterSlaveDatabase(databaseName , context , DefaultMasterSlaveRule.create(defaultXmlPath)));
                    } catch (Exception e) {
                        throw new BigSqlException(100 , "load master slave database fail " , e);
                    }
                }
            }
        }
        this.databaseMap = databaseMap;
    }




    /**
     * init database name from dir
     */
    private void initDatabase(String schemaPath) {

        File file = new File(schemaPath);
        if(!file.exists()){
            file.mkdirs();
        }

        Set<String> tmp = new HashSet<>();
        tmp.add(DEFAULT_DATABASE_NAME);
        File[] list = file.listFiles();
        if(list != null && list.length > 0){
            for (File f : list) {
                if(f.isDirectory()){
                    tmp.add(f.getName());
                }else{
                    LOG.warn(schemaPath + File.separator + f.getName() + " is not a dir");
                }
            }
        }
        this.database = tmp;
    }


    public LogicDatabase getBigsqlDatabase(){
        SimpleLogicDatabase database = new SimpleLogicDatabase(Constant.DEFAULT_DATABASE_NAME);
        Map<String,LogicTable> tables = new HashMap<>();

        tables.put(Constant.BIGSQL_CONNECTION,new ConnectionTable(Constant.BIGSQL_CONNECTION , Constant.DEFAULT_DATABASE_NAME , context));
        tables.put(Constant.BIGSQL_CONNECTION_POOL,new ConnectionPoolTable(Constant.BIGSQL_CONNECTION_POOL , Constant.DEFAULT_DATABASE_NAME , context));

        database.setTables(tables);
        return database;
    }
}
