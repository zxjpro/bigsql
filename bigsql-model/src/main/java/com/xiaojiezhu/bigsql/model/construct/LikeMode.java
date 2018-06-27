package com.xiaojiezhu.bigsql.model.construct;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/22 15:54
 * 说明 ...
 */
public enum LikeMode {
    /**
     * '%xx'
     */
    LEFT,
    /**
     * 'xx%'
     */
    RIGHT,
    /**
     * '%xx%'
     */
    DOUBLE
}
