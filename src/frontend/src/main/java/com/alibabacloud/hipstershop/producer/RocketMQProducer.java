package com.alibabacloud.hipstershop.producer;

import com.alibabacloud.hipstershop.cartserviceapi.domain.CartConsts;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author shenyi
 * @date 2020/12/30
 * @email shenyi.yt@alibaba-inc.com
 */
@Component
public class RocketMQProducer {

    private static final Logger log = LoggerFactory.getLogger(RocketMQProducer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DefaultMQProducer producer;
    private String topic;
    private String tag;

    @PostConstruct
    public void init() throws MQClientException {
        String namesrvAddr = System.getenv(CartConsts.KEY_ROCKETMQ_NAMESRV_ADDR);
        if (StringUtils.isEmpty(namesrvAddr)) {
            log.error("can not find rocketmq namesrv addr!");
            return;
        }
        String producerGroup = getEnvOrDefault(CartConsts.KEY_ROCKETMQ_PRODUCER_GROUP, CartConsts.DEF_ROCKETMQ_PRODUCER_GROUP);
        topic = getEnvOrDefault(CartConsts.KEY_ROCKETMQ_TOPIC, CartConsts.DEF_ROCKETMQ_TOPIC);
        tag =  getEnvOrDefault(CartConsts.KEY_ROCKETMQ_TAG, CartConsts.DEF_ROCKETMQ_TAG);
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
    }

    public void send(Object body) {
        if (producer == null) {
            throw new IllegalArgumentException("can not send msg to rocketmq");
        }
        byte[] bytes = null;
        try {
            String json = objectMapper.writeValueAsString(body);
            bytes = json.getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            log.error("", e);
        }
        Message message = new Message(topic, tag, bytes);
        try {
            producer.send(message);
        } catch (MQClientException e) {
            log.error("", e);
        } catch (RemotingException e) {
            log.error("", e);
        } catch (MQBrokerException e) {
            log.error("", e);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

    private String getEnvOrDefault(String key, String defaultVal) {
        String val = System.getenv(key);
        return StringUtils.isEmpty(val) ? defaultVal : val;
    }

}
