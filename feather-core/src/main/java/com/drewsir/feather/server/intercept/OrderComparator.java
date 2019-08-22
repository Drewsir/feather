package com.drewsir.feather.server.intercept;

import java.util.Comparator;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/5/21 19:45
 * @since JDK 1.8
 */
public class OrderComparator implements Comparator<FeatherInterceptor> {


    @Override
    public int compare(FeatherInterceptor o1, FeatherInterceptor o2) {

        if (o1.getOrder() <= o2.getOrder()){//重新对责任链排序
            return 1 ;
        }
        return 0;
    }
}
