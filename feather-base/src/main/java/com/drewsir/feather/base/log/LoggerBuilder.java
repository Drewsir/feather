package com.drewsir.feather.base.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function:
 *
 * @author drew
 *         Date: 2018/3/14
 * @since JDK 1.8
 */
public class LoggerBuilder {


    /**
     * get static Logger
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz){
        return LoggerFactory.getLogger(clazz);
    }
}
