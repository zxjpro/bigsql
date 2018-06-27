package com.xiaojiezhu.bigsql.common.annotation;

import java.lang.annotation.*;

/**
 * time 2018/6/25 16:54
 *
 * enable a class to be strategy
 *
 * @author xiaojie.zhu <br>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableStrategy {
}
