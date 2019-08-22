package com.drewsir.feather.server.reflect;

import org.junit.Test;

import java.util.Set;

/**
 * @author drew
 * @create 2019-04-04 15:34
 */
public class ClassScannerTest {
    @Test
    public void getFeatherClasses() throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses("com.drewsir.feather");
        System.out.println(classes.size());
    }

}