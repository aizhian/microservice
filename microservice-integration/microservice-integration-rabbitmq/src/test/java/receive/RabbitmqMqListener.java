package receive;

//import com.qingju.hcxd.activity.consumer.data.iservice.ICouponLogService;
//import com.qingju.hcxd.activity.consumer.execute.ActivityManager;
//import com.qingju.hcxd.activity.consumer.vo.CouponLog;
//import com.qingju.hcxd.common.util.JsonUtils;
//import com.qingju.hcxd.common.util.StringUtils;
//import com.qingju.hcxd.plugin.spring.protobuf.msg.CouponProto;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.Headers;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.io.IOException;

/**
 * Created by Aizhanglin on 2016/8/24.
 */
//@Component
public class RabbitmqMqListener {
//    private final Logger logger = LoggerFactory.getLogger(RabbitmqMqListener.class);
//    private final String couponActivityCode = "COUPON";
//
//    @Autowired
//    private ICouponLogService iCouponLogService;
//
//    @RabbitListener(queues = "queue.message")
//    //@Headers 获取全部请求头，@Header获取单个请求头
//    public void receiveCouponLog(@Headers Map headers, CouponProto.CouponLog couponLogProto) {
//        try {
//            Map params = new HashMap();
//            params.putAll(headers);
//
//            CouponLog couponLog = new CouponLog();
//            couponLog.setUserId(couponLogProto.getUserId());
//            couponLog.setSwiftNumber(couponLogProto.getSwiftNumber());
//            couponLog.setActionCode(couponLogProto.getActionCode());
//            couponLog.setChannelCode(couponLogProto.getChannelCode());
//            couponLog.setResponseStatus(couponLogProto.getResponseStatus());
//            couponLog.setCreateTime(new Date());
//            couponLog.setBill(couponLogProto.getBill());
//            couponLog.setClientType(couponLogProto.getClientType());
//            String referenceBill = couponLogProto.getReferenceBill();
//            if (StringUtils.isNotBlank(referenceBill)) {
//                CouponLog referenceCouponLog = iCouponLogService.findCouponLogByBill(referenceBill);
//                couponLog.setReference(referenceCouponLog);
//            }
//            params.put("couponLog", couponLog);
//            logger.info("红包消息请求参数: " + JsonUtils.toJson(params));
//
//            ActivityManager activityManager = new ActivityManager();
//            //匹配执行
//            activityManager.matchAndExec(couponActivityCode, params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @RabbitListener(queues = "queue.message")
//    public void receive(Message message, Channel channel) throws IOException {
//        //收到消息
//        message.getBody();
//        //rabbitmq 客户端同时接受消息最大数量为1 （参数1：最大内容长度限制。参数2：同时收到最大消息数，如果没有ack的消息达到该数量该客户端会block，参数3：前两项设置适用于channel
//        // 还是整个cusumer）
//        channel.basicQos(0,1,false);
//        //确认消息收到
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//        //批量决绝回信,第二个参数决定是批量还是一个，第三个参数决定返回到重回原队列，如果配置有死信转发返回到死信队列）
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
//        //批量决绝回信,第二个参数决定是批量还是一个，第三个参数决定返回到重回原队列
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
//    }
}
