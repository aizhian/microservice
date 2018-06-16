package com.genesis.microservice.integration.rabbitmq.configration;

import com.genesis.microservice.integration.message.protobuf.ProtobufMessageConverter;
import com.sun.media.jfxmedia.logging.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;

import java.nio.channels.ServerSocketChannel;

/**
 * Created by Aizhanglin on 2016/9/8.
 */
@Configuration
@EnableRabbit
public class RabbitmqConfiguration implements RabbitListenerConfigurer {
    @Value("${spring.rabbitmq.dataRoutingKey}")
    private String dataRoutingKey;
    /**
     * Shared topic exchange used for publishing any market data (e.g. stock quotes)
     */
    protected static String EXCHANGE_NAME = "app.hcxd.exchange";


    private ProtobufMessageConverter protobufMessageConverter() {
        ProtobufMessageConverter converter = new ProtobufMessageConverter();
        return converter;
    }
    private DefaultMessageHandlerMethodFactory protobufHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(protobufMessageConverter());
        return factory;
    }
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(protobufHandlerMethodFactory());
    }


//    @Bean
//    public ConnectionFactory connectionFactory() {
//        //TODO make it possible to customize in subclasses.
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        connectionFactory.setPort(port);
//        return connectionFactory;
//    }

    @Bean
    @ConditionalOnSingleCandidate(ConnectionFactory.class)
    public RabbitMessagingTemplate rabbitMessagingTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        //设置消息发送失败后回调，需要设置connectFactory.setPublisherConfirms(true)
//        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                if (!ack){//如果非应答模式，消息回被取出后会直接删除，不重置消息回队列
//                    //打印日志
//                }
//            }
//        });
        RabbitMessagingTemplate rabbitMessagingTemplate=new RabbitMessagingTemplate(template);
        rabbitMessagingTemplate.setMessageConverter(protobufMessageConverter());
//        rabbitMessagingTemplate.setMessageConverter(new MappingJackson2MessageConverter());
        return rabbitMessagingTemplate;
    }


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }


    @Bean
    public Queue queue() {
        return new Queue("queue.message");
    }

    /**
     * Binds to data exchange.
     */
    @Bean
    public Binding bindingExchangeMessage(TopicExchange topicExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(dataRoutingKey);
    }

//    @Bean
//    public StatefulRetryOperationsInterceptor statefulRetryOperationsInterceptor(){
//        return RetryInterceptorBuilder.stateful()
//                .maxAttempts(5)//重试次数
//                .backOffOptions(1000, 2.0, 10000).build();
//    }

    /* 以下配置死信转发（TTL超时，ack拒绝的信息）*/
//    @Value("${spring.rabbitmq.delay-exchange-name}")
//    private String DELAY_EXCHANGE_NAME;
//    @Value("${spring.rabbitmq.delay-process-queue-name}")
//    private String DELAY_PROCESS_QUEUE_NAME;
//    @Value("${spring.rabbitmq.queue_expiration}")
//    private String QUEUE_EXPIRATION;
//    @Value("${spring.rabbitmq.queue.msgqueue.name}")
//    private String QUEUE_MESSGAGE_NAME;
//
//
//
//    @Bean
//    public Exchange directExchange(){
//        return new DirectExchange(DELAY_EXCHANGE_NAME); //指定队列名转发路由
//    }
//    @Bean
//    public Queue msgQueue(){
//        Queue queue=QueueBuilder.durable(QUEUE_MESSGAGE_NAME)
//                .autoDelete()
//                .withArgument("x-dead-letter-exchange",DELAY_EXCHANGE_NAME)//配置死信路由
//                .withArgument("x-dead-letter-routing-key",DELAY_PROCESS_QUEUE_NAME)//配置死信转发的routingkey
//                .withArgument("x-message-ttl",QUEUE_EXPIRATION).build();//配置超时时间
//        return queue;
//    }
//    @Bean
//    public Queue deadLetterQueue(){
//        Queue queue=QueueBuilder.durable("queue.deadLetter").build();
//        return queue;
//    }
//
//    @Bean
//    public Binding bindingExchangeMessage(DirectExchange directExchange) {
//        return BindingBuilder.bind(msgQueue()).to(directExchange).with(QUEUE_MESSGAGE_NAME);
//    }

}
