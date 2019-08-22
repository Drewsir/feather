package com.drewsir.feather.server.action.res;

import io.netty.handler.codec.http.cookie.DefaultCookie;
import com.drewsir.feather.server.action.req.Cookie;
import com.drewsir.feather.server.constant.FeatherConstant;
import com.drewsir.feather.server.exception.FeatherException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/3/5 11:13
 * @since JDK 1.8
 */
public class FeatherHttpResponse implements FeatherResponse {

    private Map<String, String> headers = new HashMap<>(8);

    private String contentType;

    private String httpContent;

    private List<io.netty.handler.codec.http.cookie.Cookie> cookies = new ArrayList<>(6);

    private FeatherHttpResponse() {//构造函数私有化，防止外部破坏整体性
    }

    public static FeatherHttpResponse init() {
        FeatherHttpResponse response = new FeatherHttpResponse();
        response.contentType = FeatherConstant.ContentType.TEXT;
        return response;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setHttpContent(String content) {
        httpContent = content;
    }

    @Override
    public String getHttpContent() {
        return this.httpContent == null ? "" : this.httpContent;
    }

    public void setHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }


    @Override
    public void setCookie(Cookie FeatherCookie) {
        if (null == FeatherCookie){
            throw new FeatherException("cookie is null!") ;
        }

        if (null == FeatherCookie.getName()){
            throw new FeatherException("cookie.getName() is null!") ;
        }
        if (null == FeatherCookie.getValue()){
            throw new FeatherException("cookie.getValue() is null!") ;
        }

        DefaultCookie cookie = new DefaultCookie(FeatherCookie.getName(), FeatherCookie.getValue());

        cookie.setPath("/");
        cookie.setMaxAge(FeatherCookie.getMaxAge());
        cookies.add(cookie) ;
    }

    @Override
    public List<io.netty.handler.codec.http.cookie.Cookie> cookies() {
        return cookies;
    }


}
