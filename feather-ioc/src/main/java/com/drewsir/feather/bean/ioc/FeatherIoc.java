package com.drewsir.feather.bean.ioc;

import com.drewsir.feather.base.bean.FeatherBeanFactory;
import com.drewsir.feather.base.log.LoggerBuilder;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/6/14 01:14
 * @since JDK 1.8
 */
public class FeatherIoc implements FeatherBeanFactory {

    private static final Logger LOGGER = LoggerBuilder.getLogger(FeatherIoc.class) ;

    private static Map<String, Object> beans = new HashMap<>(16) ;

    @Override
    public void register(Object object) {
        beans.put(object.getClass().getName(), object) ;//将所有的实例都存放在一个 Map 中
    }

    @Override
    public Object getBean(String name) {
        return beans.get(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T) getBean(clazz.getName());
    }

    @Override
    public void releaseBean() {
        beans = null ;
        LOGGER.info("release all bean success.");
    }
    
}
