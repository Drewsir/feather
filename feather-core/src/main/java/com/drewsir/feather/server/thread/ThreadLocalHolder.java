package com.drewsir.feather.server.thread;

import io.netty.util.concurrent.FastThreadLocal;
import com.drewsir.feather.server.context.FeatherContext;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/9/10 19:50
 * @since JDK 1.8
 */
public class ThreadLocalHolder {

    private static final FastThreadLocal<Long> LOCAL_TIME = new FastThreadLocal() ;

    private static final FastThreadLocal<FeatherContext> Feather_CONTEXT = new FastThreadLocal() ;


    /**
     * set Feather context
     * @param context current context
     */
    public static void setFeatherContext(FeatherContext context){
        Feather_CONTEXT.set(context) ;
    }

    /**
     * remove Feather context
     */
    public static void removeFeatherContext(){
        Feather_CONTEXT.remove();
    }

    /**
     * @return get Feather context
     */
    public static FeatherContext getFeatherContext(){
        return Feather_CONTEXT.get() ;
    }

    /**
     * Set time
     * @param time current time
     */
    public static void setLocalTime(long time){
        LOCAL_TIME.set(time) ;
    }

    /**
     * Get time and remove value
     * @return get local time
     */
    public static Long getLocalTime(){
        Long time = LOCAL_TIME.get();
        LOCAL_TIME.remove();
        return time;
    }

}
