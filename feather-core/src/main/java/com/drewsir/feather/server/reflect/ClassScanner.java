package com.drewsir.feather.server.reflect;

import org.slf4j.Logger;
import com.drewsir.feather.base.bean.FeatherBeanFactory;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.server.annotation.FeatherAction;
import com.drewsir.feather.server.annotation.FeatherBean;
import com.drewsir.feather.server.annotation.Interceptor;
import com.drewsir.feather.server.bean.FeatherDefaultBean;
import com.drewsir.feather.server.configuration.AbstractFeatherConfiguration;
import com.drewsir.feather.server.configuration.ApplicationConfiguration;
import com.drewsir.feather.server.enums.StatusEnum;
import com.drewsir.feather.server.exception.FeatherException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Function: package Scanner
 *
 * @author drewsir
 *         Date: 2018/6/1 11:36
 * @since JDK 1.8
 */
public final class ClassScanner {

    private final static Logger LOGGER = LoggerBuilder.getLogger(ClassScanner.class);


    private static Map<String, Class<?>> actionMap = null;
    private static Map<Class<?>, Integer> interceptorMap = null;

    private static Set<Class<?>> classes = null;
    private static Set<Class<?>> Feather_classes = null;

    private static List<Class<?>> configurationList = null;


    /**
     * get Configuration
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<Class<?>> getConfiguration(String packageName) throws Exception {

        if (configurationList == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            //在查找所有用户自定义的配置管理类时需要手动将 Feather 内置的 ApplicationConfiguration 加入其中
            clsList.add(ApplicationConfiguration.class);


            if (clsList == null || clsList.isEmpty()) {
                return configurationList;
            }

            configurationList = new ArrayList<>(16);
            for (Class<?> cls : clsList) {

                if (cls.getSuperclass() != AbstractFeatherConfiguration.class) {
                    continue;
                }

                configurationList.add(cls) ;
            }
        }
        return configurationList;
    }
    /**
     * get @FeatherAction & @FeatherBean
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Map<String, Class<?>> getFeatherBean(String packageName) throws Exception {

        if (actionMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);//首先是获取到项目中自定义的所有类，然后判断是否加有 @FeatherAction 注解

            if (clsList == null || clsList.isEmpty()) {
                return actionMap;
            }

            actionMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {

                FeatherAction action = cls.getAnnotation(FeatherAction.class);//获取 @FeatherAction 注解
                FeatherBean bean = cls.getAnnotation(FeatherBean.class);
                if (action == null && bean == null) {
                    continue;
                }
                //若是目标类则把它缓存到一个本地 Map 中，方便下次访问时可以不再扫描直接从缓存中获取即可（因为反射很耗性能）。
                if (action != null){
                    actionMap.put(action.value() == null ? cls.getName() : action.value(), cls);
                }

                if (bean != null){
                    actionMap.put(bean.value() == null ? cls.getName() : bean.value(), cls);
                }

            }
        }
        return actionMap;
    }

    /**
     * whether is the target class
     * @param clazz
     * @param target
     * @return
     */
    public static boolean isInterface(Class<?> clazz,Class<?> target){
        for (Class<?> aClass : clazz.getInterfaces()) {
            if (aClass.getName().equals(target.getName())){
                return true ;
            }
        }
        return false ;
    }

    /**
     * get @Interceptor
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Map<Class<?>, Integer> getFeatherInterceptor(String packageName) throws Exception {

        if (interceptorMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return interceptorMap;
            }

            interceptorMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {
                Annotation annotation = cls.getAnnotation(Interceptor.class);
                if (annotation == null) {
                    continue;
                }

                Interceptor interceptor = (Interceptor) annotation;
                interceptorMap.put(cls, interceptor.order());//扫描拦截器时保存注解里的 order 值
            }
        }

        return interceptorMap;
    }

    /**
     * get All classes
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Set<Class<?>> getClasses(String packageName) throws Exception {

        if (classes == null){
            classes = new HashSet<>(32) ;

            baseScanner(packageName, classes);
        }

        return classes;
    }

    private static void baseScanner(String packageName, Set set) {
        boolean recursive = true;

        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, set);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            set.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error("IOException", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }


    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory())
                || (file.getName().endsWith(".class")));
        for (File file : files) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    LOGGER.error("ClassNotFoundException", e);
                }
            }
        }
    }


    private static final String BASE_PACKAGE = "com.drewsir.feather";

    /**
     * get custom route bean
     * @return
     * @throws Exception
     */
    public static Class<?> getBeanFactory() throws Exception {
        List<Class<?>> classList = new ArrayList<>();


        Set<Class<?>> classes = getCustomRouteBeanClasses(BASE_PACKAGE) ;
        for (Class<?> aClass : classes) {

            if (aClass.getInterfaces().length == 0){
                continue;
            }
            if (aClass.getInterfaces()[0] != FeatherBeanFactory.class){
                continue;
            }
            classList.add(aClass) ;
        }

        if (classList.size() > 2){
            throw new FeatherException(StatusEnum.DUPLICATE_IOC);
        }

        if (classList.size() == 2){
            Iterator<Class<?>> iterator = classList.iterator();
            while (iterator.hasNext()){
                if (iterator.next()== FeatherDefaultBean.class){
                    iterator.remove();
                }
            }
        }

        return classList.get(0);
    }


    public static Set<Class<?>> getCustomRouteBeanClasses(String packageName) throws Exception {

        if (Feather_classes == null){
            Feather_classes = new HashSet<>(32) ;

            baseScanner(packageName, Feather_classes);
        }

        return Feather_classes;
    }
}
