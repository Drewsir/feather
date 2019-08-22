package com.drewsir.feather.server.init;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import com.drewsir.feather.server.handle.HttpDispatcher;

/**
 * Function:
 *      初始化 Channel
 * @author drewsir
 *         Date: 17/05/2018 18:51
 * @since JDK 1.8
 */
public class FeatherInitializer extends ChannelInitializer<Channel> {
    private final HttpDispatcher httpDispatcher = new HttpDispatcher() ;

    @Override
    public void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                .addLast(new HttpRequestDecoder())//在管道末尾添加 http 请求的解码处理器，接受客户端请求
                .addLast(new HttpResponseEncoder())//http 请求的编码处理器，向客户端发送响应
                .addLast(new ChunkedWriteHandler())//支持异步写入大数据流
                .addLast(httpDispatcher)
                .addLast("logging", new LoggingHandler(LogLevel.INFO));//具有指定日志程序名称的新实例
    }
}
