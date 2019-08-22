package com.drewsir.feather.example.action;

import org.slf4j.Logger;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.example.configuration.KafkaConfiguration;
import com.drewsir.feather.example.configuration.RedisConfiguration;
import com.drewsir.feather.example.enums.StatusEnum;
import com.drewsir.feather.example.res.DemoResVO;
import com.drewsir.feather.server.action.WorkAction;
import com.drewsir.feather.server.action.param.Param;
import com.drewsir.feather.server.action.res.WorkRes;
import com.drewsir.feather.server.annotation.FeatherAction;
import com.drewsir.feather.server.configuration.ApplicationConfiguration;
import com.drewsir.feather.server.configuration.ConfigurationHolder;
import com.drewsir.feather.server.context.FeatherContext;

import java.util.concurrent.atomic.AtomicLong;

import static com.drewsir.feather.server.configuration.ConfigurationHolder.getConfiguration;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/4/31 18:52
 * @since JDK 1.8
 */
@FeatherAction(value = "demoAction")//为了在请求路由时能找到业务类
public class DemoAction implements WorkAction {


    private static final Logger LOGGER = LoggerBuilder.getLogger(DemoAction.class);

    private static AtomicLong index = new AtomicLong();

    @Override
    public void execute(FeatherContext context, Param paramMap) throws Exception {

        KafkaConfiguration configuration = (KafkaConfiguration) getConfiguration(KafkaConfiguration.class);
        RedisConfiguration redisConfiguration = (RedisConfiguration) ConfigurationHolder.getConfiguration(RedisConfiguration.class);
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);

        String brokerList = configuration.get("kafka.broker.list");
        String redisHost = redisConfiguration.get("redis.host");
        String port = applicationConfiguration.get("Feather.port");

        LOGGER.info("Configuration brokerList=[{}],redisHost=[{}] port=[{}]", brokerList, redisHost, port);

        String name = paramMap.getString("name");
        Integer id = paramMap.getInteger("id");
        LOGGER.info("name=[{}],id=[{}]", name, id);


        String url = context.request().getUrl();
        String method = context.request().getMethod();

        DemoResVO demoResVO = new DemoResVO();
        demoResVO.setIndex(index.incrementAndGet());
        demoResVO.setMsg(url + " " + method);
        WorkRes<DemoResVO> res = new WorkRes();
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        res.setDataBody(demoResVO);

        context.json(res);
    }

}
