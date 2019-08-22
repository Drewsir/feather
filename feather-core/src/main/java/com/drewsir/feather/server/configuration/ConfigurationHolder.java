package com.drewsir.feather.server.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Function:
 *      保存自定义配置管理
 * @author drewsir
 *         Date: 2018/4/9 20:05
 * @since JDK 1.8
 */
public class ConfigurationHolder {

    private static Map<String, AbstractFeatherConfiguration> config = new HashMap<>(8) ;

    /**
     * Add holder cache
     * @param key
     * @param configuration
     */
    public static void addConfiguration(String key, AbstractFeatherConfiguration configuration){
        config.put(key, configuration);
    }


    /**
     * Get class from cache by class name
     * @param clazz
     * @return
     */
    public static AbstractFeatherConfiguration getConfiguration(Class<? extends AbstractFeatherConfiguration> clazz){
        return config.get(clazz.getName()) ;
    }
}
