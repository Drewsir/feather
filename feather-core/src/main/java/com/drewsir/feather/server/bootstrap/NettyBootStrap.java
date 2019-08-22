package com.drewsir.feather.server.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import com.drewsir.feather.base.log.LoggerBuilder;
import com.drewsir.feather.server.bean.FeatherBeanManager;
import com.drewsir.feather.server.config.AppConfig;
import com.drewsir.feather.server.configuration.ApplicationConfiguration;
import com.drewsir.feather.server.constant.FeatherConstant;
import com.drewsir.feather.server.context.FeatherContext;
import com.drewsir.feather.server.init.FeatherInitializer;
import com.drewsir.feather.server.thread.ThreadLocalHolder;

import static com.drewsir.feather.server.configuration.ConfigurationHolder.getConfiguration;
import static com.drewsir.feather.server.constant.FeatherConstant.SystemProperties.APPLICATION_THREAD_SHUTDOWN_NAME;
import static com.drewsir.feather.server.constant.FeatherConstant.SystemProperties.APPLICATION_THREAD_WORK_NAME;

/**
 * Function:
 *
 * @author drewsir
 *         Date: 2018/4/10 21:56
 * @since JDK 1.8
 */
public class NettyBootStrap {

    private final static Logger LOGGER = LoggerBuilder.getLogger(NettyBootStrap.class);

    private static AppConfig appConfig = AppConfig.getInstance() ;
    private static EventLoopGroup boss = new NioEventLoopGroup(1,new DefaultThreadFactory("boss"));//线程池中有1个线程，用于获取连接
    private static EventLoopGroup work = new NioEventLoopGroup(0,new DefaultThreadFactory(APPLICATION_THREAD_WORK_NAME));//用于处理连接
    private static Channel channel ;

    /**
     * Start netty Server
     *
     * @throws Exception
     */
    public static void startFeather() throws Exception {
        // start
        startServer();

        // register shutdown hook
        shutDownServer();

        // synchronized channel
        joinServer();
    }

    /**
     * start netty server
     * @throws InterruptedException
     */
    private static void startServer() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()//创建 ServerBootstrap 对象
                .group(boss, work)//设置 EventLoopGroup，其提供了用于处理 Channel 事件的 EventLoop
                .channel(NioServerSocketChannel.class)//指定要使用的 Channel 实现
                .childHandler(new FeatherInitializer());//设置用于处理已被接受的子 Channel 的 I/O 及数据的 Handler

        ChannelFuture future = bootstrap.bind(AppConfig.getInstance().getPort()).sync();//通过配置好的 ServerBootstrap 的实例绑定该 Channel，并等待，直到异步操作执行完毕
        if (future.isSuccess()) {//绑定成功
            appLog();
        }
        channel = future.channel();//返回与此未来关联的 I/O 操作发生的通道
    }

    /**
     * joinServer
     * @throws Exception
     */
    private static void joinServer() throws Exception {
        channel.closeFuture().sync();//开启了一个 channel 的监听器，负责监听 channel 是否关闭，如果未来监听到 channel 关闭，子线程才会释放，sync()让主线程同步等待子线程结果
    }

    /**
     * appLog
     */
    private static void appLog() {
        Long start = ThreadLocalHolder.getLocalTime();
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfiguration(ApplicationConfiguration.class);
        long end = System.currentTimeMillis();
        LOGGER.info("Feather started on port: {}.cost {}ms", applicationConfiguration.get(FeatherConstant.Feather_PORT), end - start);
        LOGGER.info(">> access http://{}:{}{} <<","127.0.0.1",appConfig.getPort(),appConfig.getRootPath());
    }

    /**
     * shutdown server
     */
    private static void shutDownServer() {
        ShutDownThread shutDownThread = new ShutDownThread();
        shutDownThread.setName(APPLICATION_THREAD_SHUTDOWN_NAME);
        Runtime.getRuntime().addShutdownHook(shutDownThread);//在 JVM 中增加一个关闭的钩子，当 jvm 关闭的时候，会执行系统中已经设置的所有通过方法 addShutdownHook 添加的钩子
    }

    private static class ShutDownThread extends Thread {
        @Override
        public void run() {
            LOGGER.info("Feather server stop...");
            FeatherContext.removeContext();//移除上下文

            FeatherBeanManager.getInstance().releaseBean();//销毁所有的 bean

            boss.shutdownGracefully();//释放所有的资源，并且关闭所有的当前正在使用中的 Channel
            work.shutdownGracefully();

            LOGGER.info("Feather server has been successfully stopped.");
        }

    }
}


