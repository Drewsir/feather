package com.drewsir.feather.example.intercept;

import org.slf4j.Logger;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.server.action.param.Param;
import com.drewsir.feather.server.annotation.Interceptor;
import com.drewsir.feather.server.context.FeatherContext;
import com.drewsir.feather.server.intercept.FeatherInterceptor;

/**
 * Function: common interceptor
 *
 * @author drewsir
 *         Date: 2018/5/2 14:39
 * @since JDK 1.8
 */
@Interceptor(order = 1)
public class LoggerInterceptor extends FeatherInterceptor {

    private static final Logger LOGGER = LoggerBuilder.getLogger(LoggerInterceptor.class) ;

    @Override
    public boolean before(FeatherContext context, Param param) throws Exception {
        return super.before(context, param);
    }

    @Override
    public void after(FeatherContext context, Param param) {
        LOGGER.info("logger param=[{}]", param.toString());
    }
}
