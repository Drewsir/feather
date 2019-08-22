package com.drewsir.feather.server.configuration;

import java.util.Properties;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/6/8 16:28
 * @since JDK 1.8
 */
public abstract class AbstractFeatherConfiguration {

    /**
     * file name
     */
    private String propertiesName;//用于初始化时通过名称加载配置文件

    private Properties properties;//一个 Map 结构的缓存，用于存放所有的配置


    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public String getPropertiesName() {
        return propertiesName;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        return properties.get(key) == null ? null : properties.get(key).toString();
    }

    @Override
    public String toString() {
        return "AbstractFeatherConfiguration{" +
                "propertiesName='" + propertiesName + '\'' +
                ", properties=" + properties +
                '}';
    }
}
