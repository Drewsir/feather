package com.drewsir.feather.server;

import com.drewsir.feather.server.bootstrap.NettyBootStrap;
import com.drewsir.feather.server.config.FeatherSetting;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/3/1 12:48
 * @since JDK 1.8
 */
public final class FeatherServer {

    /**
     * Start cicada server by path
     * @param clazz
     * @param path
     * @throws Exception
     */
    public static void start(Class<?> clazz, String path) throws Exception {
        FeatherSetting.setting(clazz, path);
        NettyBootStrap.startFeather();
    }


    /**
     * Start the service through the port in the configuration file
     * @param clazz
     * @throws Exception
     */
    public static void start(Class<?> clazz) throws Exception {
        start(clazz,null);
    }
}
