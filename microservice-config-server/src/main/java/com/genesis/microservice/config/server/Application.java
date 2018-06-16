package com.genesis.microservice.config.server;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Aizhanglin on 2016/7/23.
 *
 */
@Slf4j
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class Application {
    private static volatile boolean running = true;
    public static final String SHUTDOWN_HOOK_KEY = "shutdown.hook";

    public static void main(String[] args) {
        final ConfigurableApplicationContext context
                = new SpringApplicationBuilder(Application.class)
                .web(true)
                .run(args);
        if ("true".equals(System.getProperty(SHUTDOWN_HOOK_KEY))) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        context.stop();
                        log.info("project has stopped...");
                    } catch (Throwable t) {
                        log.error(t.getMessage(), t);
                    }
                    synchronized (Application.class) {
                        running = false;
                        Application.class.notify();
                    }
                }
            });
        }
        log.info(System.getProperty(SHUTDOWN_HOOK_KEY));
        synchronized (Application.class) {
            while (running) {
                try {
                    Application.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }
}
