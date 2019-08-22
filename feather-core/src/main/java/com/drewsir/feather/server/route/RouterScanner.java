package com.drewsir.feather.server.route;

import io.netty.handler.codec.http.QueryStringDecoder;
import com.drewsir.feather.server.annotation.FeatherAction;
import com.drewsir.feather.server.annotation.FeatherRoute;
import com.drewsir.feather.server.config.AppConfig;
import com.drewsir.feather.server.context.FeatherContext;
import com.drewsir.feather.server.enums.StatusEnum;
import com.drewsir.feather.server.exception.FeatherException;
import com.drewsir.feather.server.reflect.ClassScanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/6/13 11:48
 * @since JDK 1.8
 */
public class RouterScanner {

    private static Map<String, Method> routes = null;

    private volatile static RouterScanner routerScanner;

    private AppConfig appConfig = AppConfig.getInstance();

    /**
     * get single Instance
     *
     * @return
     */
    public static RouterScanner getInstance() {
        if (routerScanner == null) {
            synchronized (RouterScanner.class) {
                if (routerScanner == null) {
                    routerScanner = new RouterScanner();
                }
            }
        }
        return routerScanner;
    }

    private RouterScanner() {
    }

    /**
     * get route method
     *
     * @param queryStringDecoder
     * @return
     * @throws Exception
     */
    public Method routeMethod(QueryStringDecoder queryStringDecoder) throws Exception {
        if (routes == null) {
            routes = new HashMap<>(16);
            loadRouteMethods(appConfig.getRootPackageName());
        }
        //default response
        boolean defaultResponse = defaultResponse(queryStringDecoder.path());

        if (defaultResponse) {
            return null;
        }
        Method method = routes.get(queryStringDecoder.path());//请求时查询映射关系

        if (method == null) {
            throw new FeatherException(StatusEnum.NOT_FOUND);
        }
        return method;


    }

    private boolean defaultResponse(String path) {
        if (appConfig.getRootPath().equals(path)) {
            FeatherContext.getContext().html("<center> Hello Feather <br/><br/>" +
                    "Power by <a href='https://github.com/Drewsir/feather'>@Feather</a> </center>");
            return true;
        }
        return false;
    }


    private void loadRouteMethods(String packageName) throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses(packageName);//获取到项目中自定义的所有类

        for (Class<?> aClass : classes) {
            Method[] declaredMethods = aClass.getMethods();

            for (Method method : declaredMethods) {
                FeatherRoute annotation = method.getAnnotation(FeatherRoute.class);//判断该方法是否加了 @FeatherRoute 注解
                if (annotation == null) {
                    continue;
                }

                FeatherAction FeatherAction = aClass.getAnnotation(FeatherAction.class);//判断该类是否加了 @FeatherAction 注解
                routes.put(appConfig.getRootPath() + "/" + FeatherAction.value() + "/" + annotation.value(), method);//将他们的映射关系存入 Map 中
            }
        }
    }
}
