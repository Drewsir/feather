package com.drewsir.feather.server.reflect;

import com.alibaba.fastjson.JSON;
import com.drewsir.feather.base.log.LoggerBuilder;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author drew
 * @create 2019-04-04 15:34
 */
public class ScannerTest {


    private static final Logger LOGGER = LoggerBuilder.getLogger(ScannerTest.class) ;

    @Test
    public void getClasses() throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses("com.drewsir.feather.server");

        LOGGER.info("classes=[{}]", JSON.toJSONString(classes));
    }


    @Test
    public void getActionAction() throws Exception{
        Map<String, Class<?>> cicadaAction = ClassScanner.getFeatherBean("com.drewsir.feather.server");
        LOGGER.info("classes=[{}]", JSON.toJSONString(cicadaAction));
    }


    @Test
    public void getConfiguration() throws Exception {
        List<Class<?>> configuration = ClassScanner.getConfiguration("top.crossoverjie.cicada.server");
        LOGGER.info("configuration=[{}]",configuration.toString());
    }


    @Test
    public void stringTest(){
        String text = "/cicada-example/routeAction/getUser" ;
        text = text.replace("/cicada-example","");
        LOGGER.info(text);
    }



}
