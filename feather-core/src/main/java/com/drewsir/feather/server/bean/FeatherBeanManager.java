package com.drewsir.feather.server.bean;

import com.drewsir.feather.base.bean.FeatherBeanFactory;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.server.exception.GlobalHandelException;
import com.drewsir.feather.server.reflect.ClassScanner;
import org.slf4j.Logger;


import java.util.Map;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/3/14 11:41
 * @since JDK 1.8
 */
public final class FeatherBeanManager {
    private final static Logger LOGGER = LoggerBuilder.getLogger(FeatherBeanManager.class);

    private FeatherBeanManager(){
    }

    private static volatile FeatherBeanManager featherBeanManager;//使用 volatile 可以禁止 JVM 的指令重排

    private static FeatherBeanFactory featherBeanFactory ;

    private GlobalHandelException handelException ;

    public static FeatherBeanManager getInstance() {//双重校验锁-单例模式
        if (featherBeanManager == null) {
            synchronized (FeatherBeanManager.class) {
                if (featherBeanManager == null) {
                    featherBeanManager = new FeatherBeanManager();
                }
            }
        }
        return featherBeanManager;
    }

    /**
     * initBean route bean factory
     * @param packageName
     * @throws Exception
     */
    public void initBean(String packageName) throws Exception {
        Map<String, Class<?>> FeatherBean = ClassScanner.getFeatherBean(packageName);

        Class<?> bean = ClassScanner.getBeanFactory();//需要根据用户的选择实例化 FeatherBeanFactory 接口
        featherBeanFactory = (FeatherBeanFactory) bean.newInstance() ;

        for (Map.Entry<String, Class<?>> classEntry : FeatherBean.entrySet()) {
            Object instance = classEntry.getValue().newInstance();
            featherBeanFactory.register(instance) ;//将所有的实例注册到 FeatherBeanFactory 接口中

            //set exception handle
            if (ClassScanner.isInterface(classEntry.getValue(), GlobalHandelException.class)){
                GlobalHandelException exception = (GlobalHandelException) instance;//找到实现了 GlobalHandelException 接口的类，将它实例化并注册到 IOC 容器中。
                FeatherBeanManager.getInstance().exceptionHandle(exception);
            }
        }

    }


    /**
     * get route bean
     * @param name
     * @return
     * @throws Exception
     */
    public Object getBean(String name) {
        try {
            return featherBeanFactory.getBean(name) ;//获取实例
        } catch (Exception e) {
            LOGGER.error("get bean error",e);
        }
        return null ;
    }

    /**
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T getBean(Class<T> clazz) {
        try {
            return featherBeanFactory.getBean(clazz) ;
        } catch (Exception e) {
            LOGGER.error("get bean error",e);
        }
        return null ;
    }

    /**
     * release all beans
     */
    public void releaseBean(){
        featherBeanFactory.releaseBean();
    }

    public void exceptionHandle(GlobalHandelException ex){
        handelException = ex ;
    }

    public GlobalHandelException exceptionHandle(){
        return handelException ;
    }
}
