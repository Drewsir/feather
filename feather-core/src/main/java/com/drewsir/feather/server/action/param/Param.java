package com.drewsir.feather.server.action.param;

import java.util.Map;

/**
 * Function:
 *      通用的 Param 接口（传递参数）
 * @author drewsir
 *         Date: 2018/3/2 11:24
 * @since JDK 1.8
 */
public interface Param extends Map<String, Object> {

    /**
     * get String
     * @param param
     * @return
     */
    String getString(String param);

    /**
     * get Integer
     * @param param
     * @return
     */
    Integer getInteger(String param);

    /**
     * get Long
     * @param param
     * @return
     */
    Long getLong(String param);

    /**
     * get Double
     * @param param
     * @return
     */
    Double getDouble(String param);

    /**
     * get Float
     * @param param
     * @return
     */
    Float getFloat(String param);

    /**
     * get Boolean
     * @param param
     * @return
     */
    Boolean getBoolean(String param) ;
}
