package com.drewsir.feather.example.action;

import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.server.action.WorkAction;
import com.drewsir.feather.server.annotation.FeatherRoute;
import org.junit.Test;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author drew
 * @create 2019-06-04 15:48
 */
public class RouteActionTest {

    private static final Logger LOGGER = LoggerBuilder.getLogger(RouteAction.class);

    @Test
    public void reflect() throws Exception {

        Map<Class<?>,Method> routes = new HashMap<>() ;

        Class<?> aClass = Class.forName("com.drewsir.feather.example.action.RouteAction");

        Method[] declaredMethods = aClass.getMethods();

        for (Method method : declaredMethods) {


            FeatherRoute annotation = method.getAnnotation(FeatherRoute.class) ;
            if (annotation == null){
                continue;
            }

            routes.put(aClass,method) ;
        }

        LOGGER.info(routes.toString());

    }

    @Test
    public void reflect2() throws Exception{
        Class<?> aClass = Class.forName("com.drewsir.feather.example.action.DemoAction");
        String name = aClass.getName();
        Class<?>[] interfaces = aClass.getInterfaces() ;
        LOGGER.info((interfaces[0].getName() == WorkAction.class.getName()) + "");
    }

    @Test
    public void reflect3(){
        try {
            Class<?> aClass = Class.forName("com.drewsir.feather.bean.ioc.FeatherIoc");
            LOGGER.info(aClass.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void costTest(){
        Map<Integer,Integer> hashmap = new HashMap<>(16);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            hashmap.put(i,i) ;
        }
        long end = System.currentTimeMillis();
        LOGGER.info("hashmap cost time=[{}] size=[{}]",(end -start),hashmap.size());

        hashmap=null;


        Map<Integer,Integer> concurrentHashMap = new ConcurrentHashMap<>(16);
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            concurrentHashMap.put(i,i) ;
        }
        end = System.currentTimeMillis();
        LOGGER.info("hashmap cost time=[{}] size=[{}]",(end -start),concurrentHashMap.size());
    }

    @Test
    public void exTest(){

        LOGGER.info("===========");
        me();
        LOGGER.info("+++++++++++");

        // LOGGER.error("e",e);

    }


    private void me(){
        LOGGER.info("me");
        return ;
    }
}
