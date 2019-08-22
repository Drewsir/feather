# feather
## 简介

基于 Netty4 实现的快速、轻量级 WEB 框架；没有过多的依赖，核心 jar 包仅 `30KB`。

## 特性

- [x] 代码简洁，没有过多依赖。
- [x] 一行代码即可启动 HTTP 服务。
- [x] [自定义拦截器](#自定义拦截器)。
- [x] [自定义全局异常](#自定义全局异常).
- [x] 灵活的传参方式。
- [x] `json` 响应格式。
- [x] [自定义配置](#自定义配置)。
- [x] 多种响应方式。
- [x] 内置可插拔 `IOC` 容器。
- [x] [`Cookie` 支持](#cookie-支持)。
- [ ] 文件上传。


## 快速启动

创建一个 maven 项目，引入核心依赖。

```xml
<dependency>
    <groupId>com.drewsir</groupId>
    <artifactId>feather-core</artifactId>
    <version>x.y.z</version>
</dependency>
```

当然也推荐额外再引入一个 `IOC` 容器插件：

```xml
<dependency>
    <groupId>com.drewsir</groupId>
    <artifactId>feather-ioc</artifactId>
    <version>2.0.1</version>
</dependency>
```

启动类：

```java
public class MainStart {

    public static void main(String[] args) throws InterruptedException {
        CicadaServer.start(MainStart.class,"/feather-example") ;
    }
}
```

### 配置业务 Action

```java
@CicadaAction("routeAction")
public class RouteAction {

    private static final Logger LOGGER = LoggerBuilder.getLogger(RouteAction.class);


    @FeatherRoute("getUser")
    public void getUser(DemoReq req){

        LOGGER.info(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        FeatherContext.getContext().json(reqWorkRes) ;
    }

    @FeatherRoute("getInfo")
    public void getInfo(DemoReq req){

        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("getInfo =" + req.toString());
        FeatherContext.getContext().json(reqWorkRes) ;
    }

    @FeatherRoute("getReq")
    public void getReq(FeatherContext context,DemoReq req){

        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("getReq =" + req.toString());
        context.json(reqWorkRes) ;
    }



}
```

启动应用访问 [http://127.0.0.1:5688/feather-example/routeAction/getUser?id=1234&name=zhangsan](http://127.0.0.1:5688/feather-example/routeAction/getUser?id=1234&name=zhangsan)

```json
{"message":"hello =zhangsan"}
```

## Feather 上下文

通过 `context.json(),context.text()` 方法可以选择不同的响应方式。

```java
@FeatherAction("routeAction")
public class RouteAction {

    private static final Logger LOGGER = LoggerBuilder.getLogger(RouteAction.class);

    @FeatherRoute("getUser")
    public void getUser(DemoReq req){

        LOGGER.info(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        FeatherContext.getContext().json(reqWorkRes) ;
    }
    
    @FeatherRoute("hello")
    public void hello() throws Exception {
        FeatherContext context = FeatherContext.getContext();

        String url = context.request().getUrl();
        String method = context.request().getMethod();
        context.text("hello world url=" + url + " method=" + method);
    }    


}
```


## Cookie 支持

### 设置 Cookie

```java
Cookie cookie = new Cookie() ;
cookie.setName("cookie");
cookie.setValue("value");
CicadaContext.getResponse().setCookie(cookie);
```

### 获取 Cookie

```java
Cookie cookie = FeatherContext.getRequest().getCookie("cookie");
logger.info("cookie = " + cookie.toString());
```

## 自定义配置

`feather` 默认会读取 classpath 下的 `application.properties` 配置文件。

同时也可以自定义配置文件。

只需要继承 `com.drewsir.feather.server.configuration.AbstractCicadaConfiguration`

并传入配置文件名称即可。比如：


```java
public class RedisConfiguration extends AbstractCicadaConfiguration {


    public RedisConfiguration() {
        super.setPropertiesName("redis.properties");
    }

}

public class KafkaConfiguration extends AbstractCicadaConfiguration {

    public KafkaConfiguration() {
        super.setPropertiesName("kafka.properties");
    }


}
```

![](https://ws3.sinaimg.cn/large/0069RVTdgy1fv5mw7p5nvj31by0fo76t.jpg)

### 获取配置

按照如下方式即可获取自定义配置：

```java
KafkaConfiguration configuration = (KafkaConfiguration) getConfiguration(KafkaConfiguration.class);
RedisConfiguration redisConfiguration = (RedisConfiguration) ConfigurationHolder.getConfiguration(RedisConfiguration.class);
ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);

String brokerList = configuration.get("kafka.broker.list");
String redisHost = redisConfiguration.get("redis.host");
String port = applicationConfiguration.get("cicada.port");

LOGGER.info("Configuration brokerList=[{}],redisHost=[{}] port=[{}]",brokerList,redisHost,port);
```

### 外置配置文件

当然在特殊环境中(`dev/test/pro`)也可以读取外置配置文件。只需要加上启动参数，保证参数名称和文件名一致即可。

```shell
-Dapplication.properties=/xx/application.properties
-Dkafka.properties=/xx/kakfa.properties
-Dredis.properties=/xx/redis.properties
```

## 自定义拦截器

实现 `com.drewsir.feather.example.intercept.FeatherInterceptor` 接口。

```java
@Interceptor(value = "executeTimeInterceptor")
public class ExecuteTimeInterceptor implements FeatherInterceptor {

    private static final Logger LOGGER = LoggerBuilder.getLogger(ExecuteTimeInterceptor.class);

    private Long start;

    private Long end;

    @Override
    public boolean before(Param param) {
        start = System.currentTimeMillis();
        return true;
    }

    @Override
    public void after(Param param) {
        end = System.currentTimeMillis();

        LOGGER.info("cast [{}] times", end - start);
    }
}
```

## 自定义全局异常

现在你可以自定义全局异常，就像这样：

```java
@FeatherBean
public class ExceptionHandle implements GlobalHandelException {
    private final static Logger LOGGER = LoggerBuilder.getLogger(ExceptionHandle.class);

    @Override
    public void resolveException(FeatherContext context, Exception e) {
        LOGGER.error("Exception", e);
        WorkRes workRes = new WorkRes();
        workRes.setCode("500");
        workRes.setMessage(e.getClass().getName());
        context.json(workRes);
    }
}
```