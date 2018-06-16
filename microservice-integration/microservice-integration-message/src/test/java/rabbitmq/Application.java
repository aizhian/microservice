package rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Aizhanglin on 2016/9/13.
 */
@SpringBootApplication
public class Application {
    private static volatile boolean running = true;
    public static final String SHUTDOWN_HOOK_KEY = "shutdown.hook";
    public static final Logger logger= LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        ConfigurableApplicationContext context
                = new SpringApplicationBuilder(Application.class)
                .web(false)
                .run(args);

        if ("true".equals(System.getProperty(SHUTDOWN_HOOK_KEY))) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        context.stop();
                        logger.info("hxcd-activity-test has stopped...");
                    } catch (Throwable t) {
                        logger.error(t.getMessage(), t);
                    }
                    synchronized (Application.class) {
                        running = false;
                        Application.class.notify();
                    }
                }
            });
        }
        logger.info(System.getProperty(SHUTDOWN_HOOK_KEY));
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
