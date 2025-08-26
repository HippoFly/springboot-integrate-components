package com.example.springbootintegratekafka.config;

import com.example.springbootintegratekafka.model.NotificationMessage;
import com.example.springbootintegratekafka.model.OrderMessage;
import com.example.springbootintegratekafka.model.UserMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka配置类
 * 展示Kafka的核心配置和最佳实践
 * 
 * 主要配置内容：
 * 1. Producer配置 - 消息生产者配置
 * 2. Consumer配置 - 消息消费者配置  
 * 3. 事务配置 - Kafka事务支持
 * 4. 序列化配置 - JSON序列化支持
 * 5. 监听器配置 - 消费者监听器配置
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:kafka-learning-group}")
    private String groupId;

    /**
     * Producer配置
     * 配置Kafka生产者的核心参数
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        
        // 基础连接配置
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        
        // 序列化配置
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        // 性能优化配置
        configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // 等待所有副本确认
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3); // 重试次数
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 批处理大小
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5); // 等待时间
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 缓冲区大小
        
        // 幂等性配置 - 确保消息不重复
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        // 事务配置
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "kafka-learning-tx");
        
        // 压缩配置
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * KafkaTemplate配置
     * Spring Kafka的核心模板类
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * 事务管理器配置
     * 支持Kafka事务操作
     */
    @Bean
    public KafkaTransactionManager<String, Object> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }

    /**
     * Consumer配置
     * 配置Kafka消费者的核心参数
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        
        // 基础连接配置
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        
        // 序列化配置
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        
        // 消费策略配置
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 从最早的消息开始消费
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 手动提交offset
        
        // 性能配置
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500); // 每次拉取的最大记录数
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024); // 最小拉取字节数
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500); // 最大等待时间
        
        // JSON反序列化配置
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.springbootintegratekafka.model");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Object.class);
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * 通用监听器容器工厂
     * 配置消费者监听器的行为
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        
        // 并发配置
        factory.setConcurrency(3); // 并发消费者数量
        
        // 确认模式配置
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        
        // 错误处理配置
        factory.setCommonErrorHandler(new org.springframework.kafka.listener.DefaultErrorHandler());
        
        return factory;
    }

    /**
     * 用户消息专用消费者工厂
     * 展示类型安全的消息消费
     */
    @Bean
    public ConsumerFactory<String, UserMessage> userMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + "-user");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.springbootintegratekafka.model");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserMessage.class);
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * 用户消息监听器容器工厂
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserMessage> userMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserMessage> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userMessageConsumerFactory());
        factory.setConcurrency(2);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * 订单消息专用消费者工厂
     */
    @Bean
    public ConsumerFactory<String, OrderMessage> orderMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + "-order");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.springbootintegratekafka.model");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OrderMessage.class);
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * 订单消息监听器容器工厂
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderMessage> orderMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderMessage> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderMessageConsumerFactory());
        factory.setConcurrency(2);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * 通知消息专用消费者工厂
     */
    @Bean
    public ConsumerFactory<String, NotificationMessage> notificationMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + "-notification");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.springbootintegratekafka.model");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, NotificationMessage.class);
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * 通知消息监听器容器工厂
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationMessage> notificationMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationMessage> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(notificationMessageConsumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}