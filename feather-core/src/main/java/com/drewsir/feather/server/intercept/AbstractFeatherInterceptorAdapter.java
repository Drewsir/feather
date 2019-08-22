package com.drewsir.feather.server.intercept;

import com.drewsir.feather.server.action.param.Param;
import com.drewsir.feather.server.context.FeatherContext;

/**
 * Function:
 *      适配器
 * @author drewsir
 *         Date: 2018/6/2 15:40
 * @since JDK 1.8
 */
public abstract class AbstractFeatherInterceptorAdapter extends FeatherInterceptor{

    @Override
    public boolean before(FeatherContext context, Param param) {
        return true;
    }

    @Override
    public void after(FeatherContext context,Param param) {

    }
}
