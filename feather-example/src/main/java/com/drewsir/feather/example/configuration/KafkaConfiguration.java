package com.drewsir.feather.example.configuration;

import com.drewsir.feather.server.configuration.AbstractFeatherConfiguration;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/5/8 16:28
 * @since JDK 1.8
 */
public class KafkaConfiguration extends AbstractFeatherConfiguration {

    public KafkaConfiguration() {
        super.setPropertiesName("kafka.properties");//代码里配置的文件名必须得和配置文件名称相同。
    }


}
