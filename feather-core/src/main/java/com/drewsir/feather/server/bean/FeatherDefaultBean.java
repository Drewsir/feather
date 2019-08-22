package com.drewsir.feather.server.bean;


import com.drewsir.feather.base.bean.FeatherBeanFactory;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/3/14 11:26
 * @since JDK 1.8
 */
public class FeatherDefaultBean implements FeatherBeanFactory {

    @Override
    public void register(Object object) {//此方式没有 bean 容器，则无需注册

    }

    @Override
    public Object getBean(String name) throws Exception {
        Class<?> aClass = Class.forName(name);
        return aClass.newInstance();//每次都会创建实例
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws Exception {
        return clazz.newInstance();
    }

    @Override
    public void releaseBean() {
    }
}
