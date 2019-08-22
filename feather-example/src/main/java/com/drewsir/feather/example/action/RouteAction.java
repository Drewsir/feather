package com.drewsir.feather.example.action;

import org.slf4j.Logger;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.example.exception.ExceptionHandle;
import com.drewsir.feather.example.req.DemoReq;
import com.drewsir.feather.server.action.req.Cookie;
import com.drewsir.feather.server.action.res.WorkRes;
import com.drewsir.feather.server.annotation.FeatherAction;
import com.drewsir.feather.server.annotation.FeatherRoute;
import com.drewsir.feather.server.bean.FeatherBeanManager;
import com.drewsir.feather.server.context.FeatherContext;

/**
 * Function:
 *      路由策略
 * @author drewsir
 *         Date: 2018/5/13 11:12
 * @since JDK 1.8
 */
@FeatherAction("routeAction")
public class RouteAction {

    private static final Logger LOGGER = LoggerBuilder.getLogger(RouteAction.class);


    @FeatherRoute("getUser")
    public void getUser(DemoReq req){

        LOGGER.info(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        FeatherContext.getContext().json(reqWorkRes) ;
    }

    @FeatherRoute("getUserText")
    public void getUserText(DemoReq req){

        LOGGER.info(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        Cookie cookie = new Cookie() ;
        cookie.setName("cookie");
        cookie.setValue(req.getName());
        FeatherContext.getResponse().setCookie(cookie);
        FeatherContext.getContext().text(req.toString());
    }

    @FeatherRoute("getInfo")
    public void getInfo(DemoReq req){

        Cookie cookie = FeatherContext.getRequest().getCookie("cookie");
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("getInfo =" + req.toString() + "cookie=" + cookie.toString());
        FeatherContext.getContext().json(reqWorkRes) ;
    }

    @FeatherRoute("getReq")
    public void getReq(FeatherContext context, DemoReq req){
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("getReq =" + req.toString());
        context.json(reqWorkRes) ;
    }


    @FeatherRoute("test")
    public void test(FeatherContext context){
        ExceptionHandle bean = FeatherBeanManager.getInstance().getBean(ExceptionHandle.class);
        LOGGER.info("====" +bean.getClass());
        context.html("<p>12345</p>");
    }

}
