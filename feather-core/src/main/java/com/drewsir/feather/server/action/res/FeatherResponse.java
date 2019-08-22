package com.drewsir.feather.server.action.res;

import com.drewsir.feather.server.action.req.Cookie;

import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/3/5 10:53
 * @since JDK 1.8
 */
public interface FeatherResponse {

    /**
     * get all customer headers
     * @return
     */
    Map<String, String> getHeaders();


    /**
     * set content type
     * @param contentType
     */
    void setContentType(String contentType);

    /**
     * get content type
     * @return
     */
    String getContentType();

    /**
     * set http body
     * @param content
     */
    void setHttpContent(String content);

    /**
     * get http body
     * @return
     */
    String getHttpContent();


    /**
     * set cookie
     * @param cookie cookie
     */
    void setCookie(Cookie cookie) ;


    /**
     * get all cookies
     * @return all cookies
     */
    List<io.netty.handler.codec.http.cookie.Cookie> cookies() ;

}
