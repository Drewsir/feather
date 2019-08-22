package com.drewsir.feather.server.intercept;

import com.drewsir.feather.server.action.param.Param;
import com.drewsir.feather.server.context.FeatherContext;

/**
 * Function: common interceptor
 *
 * @author drewsir
 *         Date: 2018/5/2 14:39
 * @since JDK 1.8
 */
public abstract class FeatherInterceptor {


    private int order ;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * before
     * @param context
     * @param param
     * @return
     * true if the execution chain should proceed with the next interceptor or the handler itself
     * @throws Exception
     */
    protected boolean before(FeatherContext context,Param param) throws Exception{
        return true;
    }


    /**
     * after
     * @param context
     * @param param
     * @throws Exception
     */
    protected void after(FeatherContext context,Param param) throws Exception{}
}
