package com.drewsir.feather.server.exception;

import com.drewsir.feather.server.context.FeatherContext;

/**
 * Function: global exception handle
 *
 * @author drewsir
 * Date: 2019-04-10 17:12
 * @since JDK 1.8
 */
public interface GlobalHandelException {

    /**
     * exception handle
     * @param context
     * @param e
     */
    void resolveException(FeatherContext context, Exception e) ;
}
