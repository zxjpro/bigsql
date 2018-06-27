package com.xiaojiezhu.bigsql.core.invoker.result;

import java.sql.ResultSet;

/**
 * select invoke result
 * @author xiaojie.zhu
 */
public class DefaultSelectInvokeResult implements SelectInvokeResult{
    private boolean success;
    private ResultSet resultSet;
    private String errorMsg;
    private int affectRow;



    @Override
    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * this method is
     * @return
     */
    @Override
    public int getAffectRow() {
        throw new RuntimeException("select statement is need call this method");
    }


    public static DefaultSelectInvokeResult createInstance(ResultSet resultSet){
        DefaultSelectInvokeResult result = new DefaultSelectInvokeResult();
        result.success = true;
        result.resultSet = resultSet;
        return result;
    }

    public static DefaultSelectInvokeResult createFailInstance(String errorMsg){
        DefaultSelectInvokeResult result = new DefaultSelectInvokeResult();
        result.errorMsg = errorMsg;
        return result;
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public void setAffectRow(int affectRow) {
        this.affectRow = affectRow;
    }


}
