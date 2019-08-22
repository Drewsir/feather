package com.drewsir.feather.server.context;

import com.alibaba.fastjson.JSON;
import com.drewsir.feather.server.action.req.FeatherRequest;
import com.drewsir.feather.server.action.res.FeatherResponse;
import com.drewsir.feather.server.action.res.WorkRes;
import com.drewsir.feather.server.constant.FeatherConstant;
import com.drewsir.feather.server.thread.ThreadLocalHolder;

/**
 * Function: Feather context
 *
 * @author drewsir
 *         Date: 2018/4/5 12:23
 * @since JDK 1.8
 */
public final class FeatherContext {


    /**
     * current thread request
     */
    private FeatherRequest request ;

    /**
     * current thread response
     */
    private FeatherResponse response ;

    public FeatherContext(FeatherRequest request, FeatherResponse response) {
        this.request = request;
        this.response = response;
    }


    /**
     * response json message
     * @param workRes
     */
    public void json(WorkRes workRes){
        FeatherContext.getResponse().setContentType(FeatherConstant.ContentType.JSON);
        FeatherContext.getResponse().setHttpContent(JSON.toJSONString(workRes));
    }

    /**
     * response text message
     * @param text response body
     */
    public void text(String text){
        FeatherContext.getResponse().setContentType(FeatherConstant.ContentType.TEXT);
        FeatherContext.getResponse().setHttpContent(text);
    }

    /**
     * response html
     * @param html response body
     */
    public void html(String html){
        FeatherContext.getResponse().setContentType(FeatherConstant.ContentType.HTML);
        FeatherContext.getResponse().setHttpContent(html);
    }

    public static FeatherRequest getRequest(){
        return FeatherContext.getContext().request ;
    }

    public FeatherRequest request(){
        return FeatherContext.getContext().request ;
    }

    public static FeatherResponse getResponse(){
        return FeatherContext.getContext().response ;
    }
    //使用 FastThreadLocal 来存放 FeatherContext
    public static void setContext(FeatherContext context){
        ThreadLocalHolder.setFeatherContext(context) ;
    }


    public static void removeContext(){
        ThreadLocalHolder.removeFeatherContext();
    }

    public static FeatherContext getContext(){
        return ThreadLocalHolder.getFeatherContext() ;
    }
}
