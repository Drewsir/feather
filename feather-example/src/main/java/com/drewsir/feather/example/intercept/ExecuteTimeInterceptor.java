package com.drewsir.feather.example.intercept;

import org.slf4j.Logger;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.server.action.param.Param;
import com.drewsir.feather.server.annotation.Interceptor;
import com.drewsir.feather.server.context.FeatherContext;
import com.drewsir.feather.server.intercept.FeatherInterceptor;

/**
 * Function:
 *      记录所有 action 的执行时间
 * @author drewsir
 *         Date: 2018/5/2 15:21
 * @since JDK 1.8
 */
@Interceptor(order = 1)
public class ExecuteTimeInterceptor extends FeatherInterceptor {

    private static final Logger LOGGER = LoggerBuilder.getLogger(ExecuteTimeInterceptor.class);

    private Long start;

    private Long end;

    @Override
    public boolean before(FeatherContext context, Param param) {
        start = System.currentTimeMillis();
        LOGGER.info("拦截请求");
        return true;
    }

    @Override
    public void after(FeatherContext context,Param param) {
        end = System.currentTimeMillis();

        LOGGER.info("cast [{}] times", end - start);
    }
}
