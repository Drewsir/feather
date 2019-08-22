package com.drewsir.feather.example;

import com.drewsir.feather.server.FeatherServer;

/**
 * @author drew
 * @create 2019-08-04 15:38
 */
public class MainStart {

    public static void main(String[] args) throws Exception {
        FeatherServer.start(MainStart.class,"/feather-example") ;
    }
}
