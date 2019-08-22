package com.drewsir.feather.server.intercept;

import com.drewsir.feather.server.action.param.Param;
import com.drewsir.feather.server.config.AppConfig;
import com.drewsir.feather.server.context.FeatherContext;
import com.drewsir.feather.server.reflect.ClassScanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/10/21 18:42
 * @since JDK 1.8
 */
public class InterceptProcess {

    private InterceptProcess(){}

    private volatile static InterceptProcess process ;

    private static List<FeatherInterceptor> interceptors ;

    private AppConfig appConfig = AppConfig.getInstance();

    /**
     * get single Instance
     * @return
     */
    public static InterceptProcess getInstance(){
        if (process == null){
            synchronized (InterceptProcess.class){
                if (process == null){
                    process = new InterceptProcess() ;
                }
            }
        }
        return process ;
    }

    //将所有的拦截器加入到责任链中
    public void loadInterceptors() throws Exception {

        if (interceptors != null){
            return;
        }else {
            interceptors = new ArrayList<>(10) ;
            Map<Class<?>, Integer> FeatherInterceptor = ClassScanner.getFeatherInterceptor(appConfig.getRootPackageName());
            for (Map.Entry<Class<?>, Integer> classEntry : FeatherInterceptor.entrySet()) {
                Class<?> interceptorClass = classEntry.getKey();
                FeatherInterceptor interceptor = (FeatherInterceptor) interceptorClass.newInstance();
                interceptor.setOrder(classEntry.getValue());//保存 order 值到拦截器中
                interceptors.add(interceptor);//加入缓存，尽量的减少反射操作
            }
            Collections.sort(interceptors, new OrderComparator());//排序
        }
    }


    /**
     * execute before
     * @param param
     * @throws Exception
     */
    public boolean processBefore(Param param) throws Exception {
        for (FeatherInterceptor interceptor : interceptors) {
            boolean access = interceptor.before(FeatherContext.getContext(), param);
            if (!access){
                return access ;
            }
        }
        return true;
    }

    /**
     * execute after
     * @param param
     * @throws Exception
     */
    public void processAfter(Param param) throws Exception{
        for (FeatherInterceptor interceptor : interceptors) {
            interceptor.after(FeatherContext.getContext(),param) ;
        }
    }
}
