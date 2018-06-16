package rabbitmq;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Aizhanglin on 2017/9/18.
 * rabbitmq测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
//指定SpringBoot工程的Application启动类
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class RabbitMqITCase {
}