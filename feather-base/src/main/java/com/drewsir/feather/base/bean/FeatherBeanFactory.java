package com.drewsir.feather.base.bean;

/**
 * Function:
 *
 * @author drew
 *         Date: 2018/3/14
 * @since JDK 1.8
 */
public interface FeatherBeanFactory {

    /**
     * Register into bean Factory
     * @param object
     */
    void register(Object object);

    /**
     * Get bean from bean Factory
     * @param name
     * @return
     * @throws Exception
     */
    Object getBean(String name) throws Exception;

    /**
     * get bean by class type
     * @param clazz
     * @param <T>
     * @return bean
     * @throws Exception
     */
    <T> T getBean(Class<T> clazz) throws Exception;

    /**
     * release all beans
     */
    void releaseBean() ;
}
