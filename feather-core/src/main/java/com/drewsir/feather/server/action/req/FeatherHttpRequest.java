package com.drewsir.feather.server.action.req;

import com.drewsir.feather.server.constant.FeatherConstant;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import com.drewsir.feather.server.constant.FeatherConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/3/5 10:42
 * @since JDK 1.8
 */
public class FeatherHttpRequest implements FeatherRequest {

    private String method ;

    private String url ;

    private String clientAddress ;

    private Map<String,Cookie> cookie = new HashMap<>(8) ;
    private Map<String,String> headers = new HashMap<>(8) ;

    private FeatherHttpRequest(){}

    //初始化
    public static FeatherHttpRequest init(DefaultHttpRequest httpRequest){
        FeatherHttpRequest request = new FeatherHttpRequest();
        request.method = httpRequest.method().name();
        request.url = httpRequest.uri();

        //build headers
        buildHeaders(httpRequest, request);

        //initBean cookies
        initCookies(request);

        return request ;
    }

    /**
     * build headers
     * @param httpRequest io.netty.httprequest
     * @param request feather request
     */
    private static void buildHeaders(DefaultHttpRequest httpRequest, FeatherHttpRequest request) {
        for (Map.Entry<String, String> entry : httpRequest.headers().entries()) {
            request.headers.put(entry.getKey(),entry.getValue());
        }
    }

    /**
     * initBean cookies
     * @param request request
     */
    private static void initCookies(FeatherHttpRequest request) {
        for (Map.Entry<String, String> entry : request.headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals(FeatherConstant.ContentType.COOKIE)){
                continue;
            }

            for (io.netty.handler.codec.http.cookie.Cookie cookie : ServerCookieDecoder.LAX.decode(value)) {//将指定的 Set-Cookie HTTP 标头值解码为 Cookie
                Cookie FeatherCookie = new Cookie() ;
                FeatherCookie.setName(cookie.name());
                FeatherCookie.setValue(cookie.value());
                FeatherCookie.setDomain(cookie.domain());
                FeatherCookie.setMaxAge(cookie.maxAge());
                FeatherCookie.setPath(cookie.path()) ;
                request.cookie.put(FeatherCookie.getName(), FeatherCookie) ;
            }
        }
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public Cookie getCookie(String key) {
        return cookie.get(key) ;
    }
}
