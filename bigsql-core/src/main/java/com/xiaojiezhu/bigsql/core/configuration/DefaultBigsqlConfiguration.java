package com.xiaojiezhu.bigsql.core.configuration;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.LikeField;
import com.xiaojiezhu.bigsql.util.BigsqlSystem;
import com.xiaojiezhu.bigsql.util.EnvUtil;
import com.xiaojiezhu.bigsql.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * time 2018/5/24 10:46
 *
 * @author xiaojie.zhu <br>
 */
public class DefaultBigsqlConfiguration implements BigsqlConfiguration {

    private BigsqlEnvironment environment;
    private BigsqlVariable variable;
    private String confPath;
    private String password;


    public DefaultBigsqlConfiguration(String password) {
        this.password = password;
        this.confPath = EnvUtil.getBigsqlConfPath();
        if(!new File(this.confPath).exists()){
            BigsqlSystem.exit(this.confPath + " not exists");
        }

        try {
            this.reload();
        } catch (Exception e) {
            throw new BigSqlException(100,"init configuration fail",e);
        }

    }

    @Override
    public void reload() throws Exception{
        loadEnvironment();
        loadVariable();
    }

    @Override
    public Entry getEnvironment(String key) {
        return environment.get(key);
    }

    @Override
    public Entry getVariable(String key) {
        return variable.get(key);
    }

    @Override
    public List<String> getVariableLikeField(LikeField likeField) {
        return variable.getFields(likeField);
    }

    @Override
    public String getConfDirPath() {
        return confPath;
    }

    @Override
    public int getExecuteConcurrent() {
        String executeConcurrent = System.getProperty("executeConcurrent");
        if(executeConcurrent == null){
            return 5;
        }else{
            return Integer.parseInt(executeConcurrent);
        }
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    private void loadEnvironment() {
        String filePath = confPath + "env"+ File.separator +"environment";
        try {
            Map<String, Entry> data = getMapEntry(filePath);
            this.environment = new SimpleBigsqlEnvironment(data);
        } catch (Exception e) {
            BigsqlSystem.exit("reload environment fail "+confPath+" , " + e.getMessage());
        }
    }


    private void loadVariable() {
        String filePath = confPath + "env"+ File.separator +"variable";

        try {
            Map<String, Entry> data = getMapEntry(filePath);
            this.variable = new SimpleBigsqlVariable(data);
        } catch (Exception e) {
            BigsqlSystem.exit("reload variable fail  "+confPath+" , " + e.getMessage());
        }
    }



    private Map<String,Entry> getMapEntry(String path)throws Exception{
        final Map<String,Entry> data = new HashMap<>(100);
        InputStream in = null;
        try {
            in = new FileInputStream(path);
            IOUtil.toString(in, new IOUtil.LineHandler() {
                @Override
                public void onLine(String line) {
                    if(!"".equals(line.trim()) && !line.startsWith("#")){
                        String[] split = line.split("=");
                        if(split.length != 2){
                            return;
                        }
                        String key = split[0].trim();
                        String value = split[1].trim();
                        try{
                            long lv = Long.parseLong(value);
                            data.put(key,new Entry(ColumnType.INT, lv));
                        }catch (Exception e){
                            try{
                                double dv = Double.parseDouble(value);
                                data.put(key,new Entry(ColumnType.FLOAT, dv));
                            }catch (Exception ee){
                                data.put(key,new Entry(ColumnType.VARCHAR, String.valueOf(value)));
                            }
                        }
                    }
                }
            });
        } finally {
            IOUtil.close(in);
        }
        return data;
    }
}
