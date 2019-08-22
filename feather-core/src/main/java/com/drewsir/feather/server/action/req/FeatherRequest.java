package com.drewsir.feather.server.action.req;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/3/5 10:40
 * @since JDK 1.8
 */
public interface FeatherRequest {

    /**
     * get request method
     * @return
     */
    String getMethod() ;

    /**
     * get request url
     * @return
     */
    String getUrl() ;

    /**
     * get cookie by key
     * @param key
     * @return return cookie by key
     */
    Cookie getCookie(String key) ;


}
