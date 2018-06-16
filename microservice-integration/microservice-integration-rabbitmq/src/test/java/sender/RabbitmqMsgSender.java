package sender;

//import com.qingju.hcxd.activity.producer.web.CouponLog;
//import com.qingju.hcxd.common.util.DateUtils;
//import com.qingju.hcxd.plugin.spring.protobuf.msg.CouponProto;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
//import org.springframework.beans.factory.server.Autowired;
//import org.springframework.beans.factory.server.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Aizhanglin on 2016/9/13.
 */
@Component
public class RabbitmqMsgSender {
//    @Value("${spring.rabbitmq.dataRoutingKey}")
//    private String dataRoutingKey;
//
//    @Autowired
//    private RabbitMessagingTemplate template;
//    @Autowired
//    private TopicExchange topicExchange;
//
//    /**
//     * 会触发红包发放的事件
//     *
//     * @param couponLog
//     */
//    public void sendCouponLog(CouponLog couponLog) {
//        int random= new Random().nextInt(89999999)+10000000;//生成8位随机数
//        String randomStr= DateUtils.getCurrentTime("yyyyMMddHHmmss")+""+random;
//
//        Map<String, Object> header = new HashMap<>();
//        header.put("contentType", "application/x-protobuf;charset=UTF-8");
//        CouponProto.CouponLog.Builder builder = CouponProto.CouponLog.newBuilder();
//        builder.setUserId(couponLog.getUserId());
//
//        builder.setSwiftNumber(randomStr);
//        builder.setActionCode(couponLog.getActionCode());
//        builder.setChannelCode(couponLog.getChannelCode());
//        builder.setResponseStatus(couponLog.getResponseStatus());
//        builder.setCreateTime(new Date().getTime());
//        builder.setBill(randomStr);
//        builder.setClientType(couponLog.getClientType());
//        if (couponLog.getReference()!=null){
//            builder.setReferenceBill(couponLog.getReference().getBill());
//        }
//
//        template.convertAndSend(topicExchange.getName(), dataRoutingKey, builder.build(), header);
//    }


}
