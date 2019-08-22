package com.drewsir.feather.server.action;

import com.drewsir.feather.server.action.param.Param;
import com.drewsir.feather.server.context.FeatherContext;
/**
 * Function:
 *      一个业务类需要实现的一个通用接口 WorkAction，想要实现具体业务只要实现它就行。
 * @author drewsir
 *         Date: 2018/3/12 15:58
 * @since JDK 1.8
 */
@Deprecated
public interface WorkAction {

    /**
     * abstract execute method
     * @param context current context
     * @param param request params
     * @throws Exception throw exception
     */
    void execute(FeatherContext context ,Param param) throws Exception;
}

