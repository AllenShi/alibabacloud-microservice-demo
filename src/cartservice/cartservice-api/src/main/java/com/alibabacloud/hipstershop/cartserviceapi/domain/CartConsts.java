package com.alibabacloud.hipstershop.cartserviceapi.domain;

/**
 * @author shenyi
 * @date 2020/12/30
 * @email shenyi.yt@alibaba-inc.com
 */
public abstract class CartConsts {

    public static final String KEY_ROCKETMQ_PRODUCER_GROUP = "rocketmq.producer.group";
    public static final String DEF_ROCKETMQ_PRODUCER_GROUP = "PID_CART";
    
    public static final String KEY_ROCKETMQ_CONSUMER_GROUP = "rocketmq.consumer.group";
    public static final String DEF_ROCKETMQ_CONSUMER_GROUP = "CID_CART";
    
    public static final String KEY_ROCKETMQ_NAMESRV_ADDR = "rocketmq.namesrv.addr";
    
    public static final String KEY_ROCKETMQ_TOPIC = "rocketmq.topic";
    public static final String DEF_ROCKETMQ_TOPIC = "T_CART";
    
    public static final String KEY_ROCKETMQ_TAG = "rocketmq.tag";
    public static final String DEF_ROCKETMQ_TAG = "cart";

}
