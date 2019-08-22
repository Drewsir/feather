package com.drewsir.feather.server.route;

import io.netty.handler.codec.http.QueryStringDecoder;
import com.drewsir.feather.server.bean.FeatherBeanManager;
import com.drewsir.feather.server.context.FeatherContext;
import com.drewsir.feather.server.enums.StatusEnum;
import com.drewsir.feather.server.exception.FeatherException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/5/13 21:18
 * @since JDK 1.8
 */
public final class RouteProcess {

    private volatile static RouteProcess routeProcess;

    private final FeatherBeanManager featherBeanManager = FeatherBeanManager.getInstance() ;

    public static RouteProcess getInstance() {
        if (routeProcess == null) {
            synchronized (RouteProcess.class) {
                if (routeProcess == null) {
                    routeProcess = new RouteProcess();
                }
            }
        }
        return routeProcess;
    }

    /**
     * invoke route method  反射调用这些方法
     * @param method
     * @param queryStringDecoder
     * @throws Exception
     */
    public void invoke(Method method, QueryStringDecoder queryStringDecoder) throws Exception {
        if (method == null){
            return;
        }

        Object[] object = parseRouteParameter(method, queryStringDecoder);
        Object bean = featherBeanManager.getBean(method.getDeclaringClass().getName());//从 bean 容器中获取实例
        if (object == null){
            method.invoke(bean) ;
        }else {
            method.invoke(bean, object);
        }
    }

    /**
     * parse route's parameter
     *
     * @param method
     * @param queryStringDecoder
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    private Object[] parseRouteParameter(Method method, QueryStringDecoder queryStringDecoder) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length == 0){
            return null;
        }

        if (parameterTypes.length > 2) {
            throw new FeatherException(StatusEnum.ILLEGAL_PARAMETER);
        }

        Object[] instances = new Object[parameterTypes.length] ;

        for (int i = 0; i < instances.length; i++) {
            //inject Feather context instance
            if (parameterTypes[i] == FeatherContext.class){
                instances[i] = FeatherContext.getContext() ;
            }else {
                //inject custom pojo
                Class<?> parameterType = parameterTypes[i];
                Object instance = parameterType.newInstance();

                Map<String, List<String>> parameters = queryStringDecoder.parameters();
                for (Map.Entry<String, List<String>> param : parameters.entrySet()) {
                    Field field = parameterType.getDeclaredField(param.getKey());
                    field.setAccessible(true);
                    field.set(instance, parseFieldValue(field, param.getValue().get(0)));
                }
                instances[i] = instance ;
            }
        }

        return instances;
    }


    private Object parseFieldValue(Field field, String value) {
        if (value == null) {
            return null;
        }

        Class<?> type = field.getType();
        if ("".equals(value)) {
            boolean base = type.equals(int.class) || type.equals(double.class) ||
                    type.equals(short.class) || type.equals(long.class) ||
                    type.equals(byte.class) || type.equals(float.class);
            if (base) {
                return 0;
            }
        }
        if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(String.class)) {
            return value;
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (type.equals(Short.class) || type.equals(short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(Byte.class) || type.equals(byte.class)) {
            return Byte.parseByte(value);
        } else if (type.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }

        return null;
    }

}
