package com.xiaojiezhu.bigsql.common;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.model.constant.Constant;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import com.xiaojiezhu.bigsql.util.ScanClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * time 2018/6/25 17:05
 *
 * @author xiaojie.zhu <br>
 */
public class DefaultClassContext implements ClassContext {
    public static final String PREFIX_PACKAGE_NAME = "com.xiaojiezhu.bigsql";

    public final static Logger LOG = LoggerFactory.getLogger(DefaultClassContext.class);
    protected Set<Class<?>> classes;
    private long reloadTime = 0L;
    private File[] jarPath;
    private boolean dev;


    private static final long INTERVAL_TIME = 5000;

    private DefaultClassContext(File ... jarPath){
        this.jarPath = jarPath;

        String property = System.getProperty(Constant.BIGSQL_DEV);
        if(!BeanUtil.isStringEmpty(property)){
            dev = Boolean.parseBoolean(property);
        }
    }

    @Override
    public Set<Class<?>> getAll() {
        return classes;
    }

    @Override
    public Set<Class<?>> getByAnnotation(Class<? extends Annotation> annotationClass) {
        if(this.classes == null){
            return null;
        }else{
            Set<Class<?>> buf = new HashSet<>();
            for (Class<?> aClass : classes) {
                Annotation annotation = aClass.getAnnotation(annotationClass);
                if(annotation != null){
                    buf.add(aClass);
                }
            }
            return buf;
        }
    }


    @Override
    public final void reload() throws Exception {
        if(System.currentTimeMillis() - reloadTime > INTERVAL_TIME){
            this.reloadTime = System.currentTimeMillis();
            Set<Class<?>> tmp = loadClass();
            this.classes = tmp;
        }else{
            LOG.warn("reload ClassContext to often");
        }

    }

    protected Set<Class<?>> loadClass(){
        if(!this.dev){
            if(this.jarPath != null && this.jarPath.length > 0){
                Set<Class<?>> classes = new LinkedHashSet<>();
                for (File file : jarPath) {
                    try {
                        ScanClassUtil.findJarClass(PREFIX_PACKAGE_NAME , file,classes);
                    } catch (Exception e) {
                        throw new BigSqlException(400 , "load class fail" , e);
                    }
                }
                return classes;
            }else{
                throw new BigSqlException(400 , "load jar path error");
            }
        }else {
            Set<Class<?>> aClass = ScanClassUtil.findClass(true);
            return aClass;
        }
    }

    public static DefaultClassContext newInstance(File ... jarPath) throws Exception {
        DefaultClassContext context = new DefaultClassContext(jarPath);
        context.reload();
        return context;
    }
}
