package com.drewsir.feather.server.config;

import com.drewsir.feather.server.FeatherServer;
import com.drewsir.feather.server.bean.FeatherBeanManager;
import com.drewsir.feather.server.configuration.AbstractFeatherConfiguration;
import com.drewsir.feather.server.configuration.ApplicationConfiguration;
import com.drewsir.feather.server.configuration.ConfigurationHolder;
import com.drewsir.feather.server.constant.FeatherConstant;
import com.drewsir.feather.server.exception.FeatherException;
import com.drewsir.feather.server.reflect.ClassScanner;
import com.drewsir.feather.server.thread.ThreadLocalHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static com.drewsir.feather.server.configuration.ConfigurationHolder.getConfiguration;
import static com.drewsir.feather.server.constant.FeatherConstant.SystemProperties.APPLICATION_THREAD_MAIN_NAME;
import static com.drewsir.feather.server.constant.FeatherConstant.SystemProperties.LOGO;

/**
 * @author drew
 * @create 2018-04-03 23:30
 */
public final class FeatherSetting {

    /**
     * @param clazz
     * @param rootPath
     * @throws Exception
     */
    public static void setting(Class<?> clazz, String rootPath) throws Exception {

        // Feather logo
        logo();

        //Initialize the application configuration
        initConfiguration(clazz);

        //Set application configuration
        setAppConfig(rootPath);

        //initBean route bean factory
        FeatherBeanManager.getInstance().initBean(rootPath);
    }


    private static void logo() {
        System.out.println(LOGO);
        Thread.currentThread().setName(APPLICATION_THREAD_MAIN_NAME) ;
    }


    /**
     * Set application configuration
     *
     * @param rootPath
     */
    private static void setAppConfig(String rootPath) {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfiguration(ApplicationConfiguration.class);

        if (rootPath == null) {
            rootPath = applicationConfiguration.get(FeatherConstant.ROOT_PATH);
        }
        String port = applicationConfiguration.get(FeatherConstant.Feather_PORT);

        if (rootPath == null) {
            throw new FeatherException("No [Feather.root.path] exists ");
        }
        if (port == null) {
            throw new FeatherException("No [Feather.port] exists ");
        }
        AppConfig.getInstance().setRootPath(rootPath);
        AppConfig.getInstance().setPort(Integer.parseInt(port));
    }


    /**
     * Initialize the application configuration
     *
     * @param clazz
     * @throws Exception
     */
    private static void initConfiguration(Class<?> clazz) throws Exception {
        ThreadLocalHolder.setLocalTime(System.currentTimeMillis());
        AppConfig.getInstance().setRootPackageName(clazz);

        //在初始化时需要找出所有继承了 AbstractFeatherConfiguration 的类
        List<Class<?>> configuration = ClassScanner.getConfiguration(AppConfig.getInstance().getRootPackageName());
        for (Class<?> aClass : configuration) {
            AbstractFeatherConfiguration conf = (AbstractFeatherConfiguration) aClass.newInstance();

            // First read
            InputStream stream ;
            String systemProperty = System.getProperty(conf.getPropertiesName());//优先读取的是 VM 启动参数中的配置文件
            if (systemProperty != null) {
                stream = new FileInputStream(new File(systemProperty));
            } else {
                stream = FeatherServer.class.getClassLoader().getResourceAsStream(conf.getPropertiesName());//通过反射创建对象
            }

            Properties properties = new Properties();
            properties.load(stream);
            conf.setProperties(properties);

            // add configuration cache
            ConfigurationHolder.addConfiguration(aClass.getName(), conf);
        }
    }
}

