package com.xiaojiezhu.bigsql.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * time 2018/6/25 18:43
 *
 * @author xiaojie.zhu <br>
 */
public class BigsqlClassLoader extends URLClassLoader {


    public BigsqlClassLoader() {
        super(new  URL[]{});
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> aClass = super.findClass(name);
        System.out.println(aClass);
        return aClass;
    }

    public BigsqlClassLoader(ClassLoader parent) {
        super(new  URL[]{}, parent);
    }

    public void addJar(File file) throws MalformedURLException {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                for (File f : files) {
                    addJar(f);
                }
            }
        }else{
            super.addURL(file.toURI().toURL());
        }
    }
}
