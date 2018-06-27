package com.xiaojiezhu.bigsql.core.invoker.result;

/**
 * //TODO not working
 * @author xiaojie.zhu
 */
public class ExecuteInvokeResult implements InvokeResult {
    protected boolean result;
    private int affectRow;
    private String errMsg;


    public ExecuteInvokeResult(boolean result) {
        this.result = result;
    }


    public ExecuteInvokeResult(boolean result, int affectRow, String errMsg) {
        this.result = result;
        this.affectRow = affectRow;
        this.errMsg = errMsg;
    }

    public ExecuteInvokeResult() {
        this.result = true;
    }

    @Override
    public boolean isSuccess() {
        return result;
    }

    @Override
    public String getErrorMsg() {
        return errMsg;
    }

    @Override
    public int getAffectRow() {
        return affectRow;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setAffectRow(int affectRow) {
        this.affectRow = affectRow;
    }



    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
