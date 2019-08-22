package com.drewsir.feather.server.util;

import com.drewsir.feather.server.config.AppConfig;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/9/1 17:46
 * @since JDK 1.8
 */
public class PathUtil {


    /**
     * Get Root Path
     * /feather-example/demoAction
     * @param path
     * @return feather-example
     */
    public static String getRootPath(String path) {
        return "/" + path.split("/")[1];
    }

    /**
     * Get Action Path
     * /feather-example/demoAction
     * @param path
     * @return demoAction
     */
    public static String getActionPath(String path) {
        return path.split("/")[2];
    }

    /**
     * Get Action Path
     * /feather-example/routeAction/getUser
     * @param path
     * @return getUser
     */
    public static String getRoutePath(String path) {
        AppConfig instance = AppConfig.getInstance();
        return path.replace(instance.getRootPackageName(),"") ;
    }


}
