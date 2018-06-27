package com.xiaojiezhu.bigsql.common;

import com.xiaojiezhu.bigsql.common.Reloadable;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * time 2018/6/25 17:02
 *
 * handler the class
 * @author xiaojie.zhu <br>
 */
public interface ClassContext extends Reloadable {

    /**
     * get all of the class
     * @return classes
     */
    Set<Class<?>> getAll();

    /**
     * get class by annotation
     * @param annotation
     * @return
     */
    Set<Class<?>> getByAnnotation(Class<? extends Annotation> annotation);
}
