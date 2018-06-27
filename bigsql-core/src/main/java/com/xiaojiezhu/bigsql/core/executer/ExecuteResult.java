package com.xiaojiezhu.bigsql.core.executer;

/**
 * @author xiaojie.zhu
 */
public class ExecuteResult {

    private boolean result;

    private Exception e;

    private Object data;

    public ExecuteResult() {
    }

    public ExecuteResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public <T> T getData() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
