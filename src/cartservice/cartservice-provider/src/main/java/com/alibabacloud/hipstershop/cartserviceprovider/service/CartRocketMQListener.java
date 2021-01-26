package com.alibabacloud.hipstershop.cartserviceprovider.service;

import com.alibabacloud.hipstershop.cartserviceapi.domain.CartConsts;
import com.alibabacloud.hipstershop.cartserviceapi.domain.CartDTO;
import com.alibabacloud.hipstershop.cartserviceapi.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author shenyi
 * @date 2020/12/30
 * @email shenyi.yt@alibaba-inc.com
 */
@Component
public class CartRocketMQListener {

    private static final Logger log = LoggerFactory.getLogger(CartRocketMQListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    CartService cartService;

    @PostConstruct
    public void init() {
        String namesrvAddr = System.getenv(CartConsts.KEY_ROCKETMQ_NAMESRV_ADDR);
        if (StringUtils.isEmpty(namesrvAddr)) {
            log.error("can not find rocketmq namesrv addr!");
            return;
        }
        String consumerGroup = getEnvOrDefault(CartConsts.KEY_ROCKETMQ_CONSUMER_GROUP, CartConsts.DEF_ROCKETMQ_CONSUMER_GROUP);
        String topic = getEnvOrDefault(CartConsts.KEY_ROCKETMQ_TOPIC, CartConsts.DEF_ROCKETMQ_TOPIC);
        String tag = getEnvOrDefault(CartConsts.KEY_ROCKETMQ_TAG, CartConsts.DEF_ROCKETMQ_TAG);

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setConsumeTimeout(30000);
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            consumer.subscribe(topic, tag);
        } catch (MQClientException e) {
            log.error("", e);
        }

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                log.info("{} Receive New Messages: {} %n", Thread.currentThread().getName(), msgs);
                for (MessageExt msg : msgs) {
                    String json = new String(msg.getBody(), StandardCharsets.UTF_8);
                    try {
                        CartDTO cartDTO = objectMapper.readValue(json, CartDTO.class);
                        cartService.addItemToCart(cartDTO.getUserId(), cartDTO.getProductId(), cartDTO.getQuantity(), cartDTO.getPrice());
                    } catch (JsonProcessingException e) {
                        log.error("", e);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者实例
        try {
            consumer.start();
        } catch (MQClientException e) {
            log.error("", e);
        }
        log.info("Consumer Started.%n");
    }

    private String getEnvOrDefault(String key, String defaultVal) {
        String val = System.getenv(key);
        return StringUtils.isEmpty(val) ? defaultVal : val;
    }
}
