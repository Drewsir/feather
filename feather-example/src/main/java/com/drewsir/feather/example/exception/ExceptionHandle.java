package com.drewsir.feather.example.exception;

import org.slf4j.Logger;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.server.action.res.WorkRes;
import com.drewsir.feather.server.annotation.FeatherBean;
import com.drewsir.feather.server.context.FeatherContext;
import com.drewsir.feather.server.exception.GlobalHandelException;

/**
 * Function:
 *
 * @author drewsir
 * Date: 2019-06-11 12:07
 * @since JDK 1.8
 */

@FeatherBean
public class ExceptionHandle implements GlobalHandelException {
    private final static Logger LOGGER = LoggerBuilder.getLogger(ExceptionHandle.class);

    @Override
    public void resolveException(FeatherContext context, Exception e) {
        LOGGER.error("Exception", e);
        WorkRes workRes = new WorkRes();
        workRes.setCode("500");
        workRes.setMessage(e.getClass().getName() + "系统运行出现异常");
        context.json(workRes);
    }
}
