package com.drewsir.feather.example.configuration;

import com.drewsir.feather.server.configuration.AbstractFeatherConfiguration;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/5/8 16:28
 * @since JDK 1.8
 */
public class RedisConfiguration extends AbstractFeatherConfiguration {


    public RedisConfiguration() {
        super.setPropertiesName("redis.properties");
    }

}
