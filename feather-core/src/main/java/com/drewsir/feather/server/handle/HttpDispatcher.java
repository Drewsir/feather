package com.drewsir.feather.server.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import org.slf4j.Logger;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.server.action.param.Param;
import com.drewsir.feather.server.action.param.ParamMap;
import com.drewsir.feather.server.action.req.FeatherHttpRequest;
import com.drewsir.feather.server.action.req.FeatherRequest;
import com.drewsir.feather.server.action.res.FeatherHttpResponse;
import com.drewsir.feather.server.action.res.FeatherResponse;
import com.drewsir.feather.server.bean.FeatherBeanManager;
import com.drewsir.feather.server.config.AppConfig;
import com.drewsir.feather.server.constant.FeatherConstant;
import com.drewsir.feather.server.context.FeatherContext;
import com.drewsir.feather.server.exception.FeatherException;
import com.drewsir.feather.server.exception.GlobalHandelException;
import com.drewsir.feather.server.intercept.InterceptProcess;
import com.drewsir.feather.server.route.RouteProcess;
import com.drewsir.feather.server.route.RouterScanner;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/4/30 18:47
 * @since JDK 1.8
 */
//标注一个 Channel Handler 可以在多个 Channel 中安全地共享使用
@ChannelHandler.Sharable
public final class HttpDispatcher extends SimpleChannelInboundHandler<DefaultHttpRequest> {//自动释放资源

    private static final Logger LOGGER = LoggerBuilder.getLogger(HttpDispatcher.class);

    private final AppConfig appConfig = AppConfig.getInstance();
    private final InterceptProcess interceptProcess = InterceptProcess.getInstance();
    private final RouterScanner routerScanner = RouterScanner.getInstance();
    private final RouteProcess routeProcess = RouteProcess.getInstance() ;
    private final FeatherBeanManager featherBeanManager = FeatherBeanManager.getInstance() ;
    private final GlobalHandelException exceptionHandle = featherBeanManager.exceptionHandle() ;
    private Exception exception;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest httpRequest) {

        //初始化上下文及赋值
        FeatherRequest FeatherRequest = FeatherHttpRequest.init(httpRequest);
        FeatherResponse FeatherResponse = FeatherHttpResponse.init();

        //将当前请求的上下文保存到了 FeatherContext 中
        FeatherContext.setContext(new FeatherContext(FeatherRequest, FeatherResponse));

        try {
            // request uri
            String uri = FeatherRequest.getUrl();
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(URLDecoder.decode(httpRequest.uri(), "utf-8"));

            // check Root Path
            appConfig.checkRootPath(uri, queryStringDecoder);

            // route Action
            //Class<?> actionClazz = routeAction(queryStringDecoder, appConfig);

            //build paramMap
            Param paramMap = buildParamMap(queryStringDecoder);

            //load interceptors
            interceptProcess.loadInterceptors();

            //interceptor before
            boolean access = interceptProcess.processBefore(paramMap);
            if (!access) {
                return;
            }

            // execute Method
            Method method = routerScanner.routeMethod(queryStringDecoder);
            routeProcess.invoke(method,queryStringDecoder) ;


            //WorkAction action = (WorkAction) actionClazz.newInstance();
            //action.execute(FeatherContext.getContext(), paramMap);


            // interceptor after
            interceptProcess.processAfter(paramMap);

        } catch (Exception e) {
            exceptionCaught(ctx, e);
        } finally {
            // Response
            responseContent(ctx);

            // remove Feather thread context
            FeatherContext.removeContext();
        }


    }


    /**
     * Response
     *
     * @param ctx
     */
    private void responseContent(ChannelHandlerContext ctx) {
        FeatherResponse FeatherResponse = FeatherContext.getResponse();
        String context = FeatherResponse.getHttpContent() ;

        ByteBuf buf = Unpooled.wrappedBuffer(context.getBytes(StandardCharsets.UTF_8));
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        buildHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * build paramMap
     *
     * @param queryStringDecoder
     * @return
     */
    private Param buildParamMap(QueryStringDecoder queryStringDecoder) {
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        Param paramMap = new ParamMap();
        for (Map.Entry<String, List<String>> stringListEntry : parameters.entrySet()) {
            String key = stringListEntry.getKey();
            List<String> value = stringListEntry.getValue();
            paramMap.put(key, value.get(0));//将接口中的参数封装到 paramMap 中
        }
        return paramMap;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        exception = (Exception) cause;
        if (FeatherException.isResetByPeer(cause.getMessage())){
            return;
        }

        if (exceptionHandle != null){
            exceptionHandle.resolveException(FeatherContext.getContext(),exception);
        }
    }

    /**
     * build Header
     *
     * @param response
     */
    private void buildHeader(DefaultFullHttpResponse response) {
        FeatherResponse FeatherResponse = FeatherContext.getResponse();

        HttpHeaders headers = response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE, FeatherResponse.getContentType());

        List<Cookie> cookies = FeatherResponse.cookies();
        for (Cookie cookie : cookies) {
            headers.add(FeatherConstant.ContentType.SET_COOKIE, io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookie));
        }

    }
}
