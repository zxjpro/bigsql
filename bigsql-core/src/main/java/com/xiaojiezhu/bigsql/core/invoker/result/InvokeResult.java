package com.xiaojiezhu.bigsql.core.invoker.result;

/**
 * @author xiaojie.zhu
 */
public interface InvokeResult {

    /**
     * the invoke is success
     * @return
     */
    boolean isSuccess();

    /**
     * if not success,it must be has err info
     * @return
     */
    String getErrorMsg();

    int getAffectRow();


}
