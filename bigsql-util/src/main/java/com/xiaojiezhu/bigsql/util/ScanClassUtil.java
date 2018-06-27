package com.xiaojiezhu.bigsql.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author 朱小杰
 * @日期 2016年10月26日 下午3:41:11
 * @版本 1.0
 * @说明 [说明这个类的作用]
 */
public class ScanClassUtil {

    /**
     * @return 返回项目中所有的类
     * @说明 查找class，默认不扫描jar文件，默认扫描当前项目的包
     * @时间 2016年10月26日 下午3:42:27
     */
    public static Set<Class<?>> findClass() {
        return findClass("", false);
    }

    /**
     * @param hasJar 是否需要扫描jar包中的类
     * @return 返回项目中所有的类
     * @说明 查找class，默认扫描当前项目的包
     * @时间 2016年10月26日 下午3:43:39
     */
    public static Set<Class<?>> findClass(boolean hasJar) {
        return findClass("", hasJar);
    }

    /**
     * @param pack   包名
     * @param hasJar 如果为true，则会扫描jar包，如果为false，则只会扫描当前项目的类。(重要：如果传入的包名为空，则这个参数不启作用，将只会扫描当前项目的类)
     * @return 返回所有的
     * @说明 查找class，可以指定包名，以及是否扫描jar包
     * @时间 2016年10月26日 下午3:45:42
     */
    public static Set<Class<?>> findClass(String pack, boolean hasJar) {
        return getClasses(pack, hasJar);
    }

    /**
     * 寻找在类型上面包含指定注解的类
     *
     * @param anotation 注解
     * @return
     * @since 2016年10月26日 下午5:35:52
     */
    @SuppressWarnings("unused")
    public static <A extends Annotation> Set<Class<?>> findClassWithAnotation(Class<A> anotation) {
        Set<Class<?>> classes = findClass();
        Iterator<Class<?>> iterator = classes.iterator();
        while (iterator.hasNext()) {
            try {
                Class<?> clazz = iterator.next();
                A annotation = clazz.getAnnotation(anotation);
                if (null == annotation) {
                    iterator.remove();
                }
            } catch (Exception e) {
            }
        }
        return classes;
    }

    public static Set<Class<?>> getClasses(String pack, boolean hasJar) {
        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = null;
        packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    if (!hasJar) {
                        //不扫描jar包
                        continue;
                    }
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }


    public static void findJarClass(String prefixPackage,File file , Set<Class<?>> classes) throws ClassNotFoundException, IOException {
        if(!file.exists()){
            return;
        }else{
            if(file.isDirectory()){
                File[] files = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        String name = pathname.getName();
                        return name.endsWith(".jar") || pathname.isDirectory();
                    }
                });
                for (File f : files) {
                    findJarClass(prefixPackage , f , classes);
                }
            }else{
                JarFile jar = null;
                try {
                    jar = new JarFile(file);
                    // 从此jar包 得到一个枚举类
                    Enumeration<JarEntry> entries = jar.entries();
                    // 同样的进行循环迭代
                    while (entries.hasMoreElements()) {
                        // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        // 如果是以/开头的
                        if (name.charAt(0) == '/') {
                            // 获取后面的字符串
                            name = name.substring(1);
                        }
                        // 如果前半部分和定义的包名相同
                        int idx = name.lastIndexOf('/');
                        // 如果可以迭代下去 并且是一个包
                        if ((idx != -1)) {
                            // 如果是一个.class文件 而且不是目录
                            if (name.endsWith(".class") && !entry.isDirectory()) {
                                // 去掉后面的".class" 获取真正的类名
                                String className = name.substring(0, name.length() - 6);
                                className = className.replace('/', '.');
                                // 添加到classes
                                if(className.startsWith(prefixPackage)){
                                    try {
                                        classes.add(Class.forName(className));
                                    } catch (Throwable e) {
                                        System.out.println("errrrr");
                                    }
                                }
                            }
                        }
                    }
                }finally {
                    IOUtil.close(jar);
                }
            }
        }

    }


    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    private static void findAndAddClassesInPackageByFile(String packageName,
                                                         String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                String path = null;
                if (!"".equals(packageName)) {
                    path = packageName + "." + file.getName();
                } else {
                    path = file.getName();
                }
                findAndAddClassesInPackageByFile(path, file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}