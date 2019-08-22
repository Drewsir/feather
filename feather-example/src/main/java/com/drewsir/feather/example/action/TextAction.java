package com.drewsir.feather.example.action;

import com.drewsir.feather.server.annotation.FeatherAction;
import com.drewsir.feather.server.annotation.FeatherRoute;
import com.drewsir.feather.server.context.FeatherContext;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/5/5 12:34
 * @since JDK 1.8
 */
@FeatherAction("textAction")
public class TextAction  {

    @FeatherRoute("hello")
    public void hello() throws Exception {
        FeatherContext context = FeatherContext.getContext();

        String url = context.request().getUrl();
        String method = context.request().getMethod();
        context.text("hello world url=" + url + " method=" + method);
    }

    @FeatherRoute("hello2")
    public void hello2(FeatherContext context) throws Exception {

        String url = context.request().getUrl();
        String method = context.request().getMethod();
        context.text("hello world2 url=" + url + " method=" + method);
    }
}
