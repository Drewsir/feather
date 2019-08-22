package com.drewsir.feather.server.configuration;

import com.drewsir.feather.server.constant.FeatherConstant;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/4/8 16:28
 * @since JDK 1.8
 */
public class ApplicationConfiguration extends AbstractFeatherConfiguration {

    public ApplicationConfiguration() {
        super.setPropertiesName(FeatherConstant.SystemProperties.APPLICATION_PROPERTIES);
    }


}
