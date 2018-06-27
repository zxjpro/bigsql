package com.xiaojiezhu.bigsql.sharding.rule.masterslave;

import java.util.List;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public class MasterSlaveDatasource {

    private List<String> masterDatasourceName;
    private List<String> slaveDatasourceName;


    public MasterSlaveDatasource() {
    }

    public MasterSlaveDatasource(List<String> masterDatasourceName, List<String> slaveDatasourceName) {
        this.masterDatasourceName = masterDatasourceName;
        this.slaveDatasourceName = slaveDatasourceName;
    }

    public List<String> getMasterDatasourceName() {
        return masterDatasourceName;
    }

    public void setMasterDatasourceName(List<String> masterDatasourceName) {
        this.masterDatasourceName = masterDatasourceName;
    }

    public List<String> getSlaveDatasourceName() {
        return slaveDatasourceName;
    }

    public void setSlaveDatasourceName(List<String> slaveDatasourceName) {
        this.slaveDatasourceName = slaveDatasourceName;
    }

    @Override
    public String toString() {
        return "master : " + masterDatasourceName + "\t" + "slave : " + slaveDatasourceName;
    }
}
