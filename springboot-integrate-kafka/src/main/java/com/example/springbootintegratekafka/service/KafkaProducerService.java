package com.example.springbootintegratekafka.service;

import com.example.springbootintegratekafka.model.NotificationMessage;
import com.example.springbootintegratekafka.model.OrderMessage;
import com.example.springbootintegratekafka.model.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Kafka生产者服务
 * 展示Kafka消息发送的各种模式和特性
 * 
 * 主要功能：
 * 1. 基础消息发送 - 简单的消息发送
 * 2. 异步消息发送 - 带回调的异步发送
 * 3. 同步消息发送 - 阻塞等待发送结果
 * 4. 分区消息发送 - 指定分区发送
 * 5. 带头部消息发送 - 添加消息头部信息
 * 6. 事务消息发送 - 事务性消息发送
 * 7. 批量消息发送 - 批量发送多条消息
 */
@Slf4j
@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // 定义Topic常量
    public static final String USER_TOPIC = "user-events";
    public static final String ORDER_TOPIC = "order-events";
    public static final String NOTIFICATION_TOPIC = "notification-events";
    public static final String GENERAL_TOPIC = "general-events";

    /**
     * 基础消息发送
     * 最简单的消息发送方式，fire-and-forget模式
     */
    public void sendMessage(String topic, Object message) {
        log.info("发送消息到Topic: {}, 消息内容: {}", topic, message);
        kafkaTemplate.send(topic, message);
    }

    /**
     * 带Key的消息发送
     * Key用于消息分区，相同Key的消息会发送到同一分区
     */
    public void sendMessageWithKey(String topic, String key, Object message) {
        log.info("发送带Key的消息到Topic: {}, Key: {}, 消息内容: {}", topic, key, message);
        kafkaTemplate.send(topic, key, message);
    }

    /**
     * 异步消息发送（带回调）
     * 发送消息后注册回调函数处理发送结果
     */
    public void sendMessageAsync(String topic, String key, Object message) {
        log.info("异步发送消息到Topic: {}, Key: {}", topic, key);
        
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
        
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(@Nullable SendResult<String, Object> result) {
                if (result != null) {
                    log.info("消息发送成功 - Topic: {}, Partition: {}, Offset: {}", 
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }
            }

            @Override
            public void onFailure(@NonNull Throwable ex) {
                log.error("消息发送失败 - Topic: {}, Key: {}, 错误: {}", topic, key, ex.getMessage(), ex);
            }
        });
    }

    /**
     * 同步消息发送
     * 阻塞等待发送结果，确保消息发送成功
     */
    public SendResult<String, Object> sendMessageSync(String topic, String key, Object message) {
        log.info("同步发送消息到Topic: {}, Key: {}", topic, key);
        
        try {
            SendResult<String, Object> result = kafkaTemplate.send(topic, key, message).get();
            log.info("同步发送成功 - Partition: {}, Offset: {}", 
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error("同步发送失败 - Topic: {}, Key: {}, 错误: {}", topic, key, e.getMessage(), e);
            throw new RuntimeException("消息发送失败", e);
        }
    }

    /**
     * 指定分区发送消息
     * 直接指定消息发送到哪个分区
     */
    public void sendMessageToPartition(String topic, Integer partition, String key, Object message) {
        log.info("发送消息到指定分区 - Topic: {}, Partition: {}, Key: {}", topic, partition, key);
        kafkaTemplate.send(topic, partition, key, message);
    }

    /**
     * 带消息头部发送
     * 在消息中添加额外的元数据信息
     */
    public void sendMessageWithHeaders(String topic, String key, Object message, String source, String version) {
        log.info("发送带头部的消息到Topic: {}, Key: {}", topic, key);
        
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, key, message);
        
        // 添加消息头部
        record.headers().add(new RecordHeader("source", source.getBytes(StandardCharsets.UTF_8)));
        record.headers().add(new RecordHeader("version", version.getBytes(StandardCharsets.UTF_8)));
        record.headers().add(new RecordHeader("timestamp", String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8)));
        
        kafkaTemplate.send(record);
    }

    /**
     * 事务性消息发送
     * 确保多条消息要么全部发送成功，要么全部失败
     */
    @Transactional("kafkaTransactionManager")
    public void sendMessagesInTransaction(List<Object> messages) {
        log.info("开始事务性发送 {} 条消息", messages.size());
        
        try {
            for (int i = 0; i < messages.size(); i++) {
                Object message = messages.get(i);
                String key = "tx-" + i;
                kafkaTemplate.send(GENERAL_TOPIC, key, message);
                log.debug("事务中发送消息 {}: {}", i + 1, message);
            }
            log.info("事务性发送完成，共发送 {} 条消息", messages.size());
        } catch (Exception e) {
            log.error("事务性发送失败，将回滚所有消息", e);
            throw e;
        }
    }

    /**
     * 发送用户事件消息
     * 业务特定的消息发送方法
     */
    public void sendUserEvent(Long userId, String username, String operation, String content) {
        UserMessage userMessage = UserMessage.create(userId, username, operation, content);
        userMessage.setDepartmentId(1L); // 示例部门ID
        
        String key = "user-" + userId;
        sendMessageWithHeaders(USER_TOPIC, key, userMessage, "user-service", "1.0");
        
        log.info("发送用户事件 - 用户ID: {}, 操作: {}", userId, operation);
    }

    /**
     * 发送订单事件消息
     * 展示复杂业务对象的消息发送
     */
    public void sendOrderEvent(String orderId, Long userId, String status, BigDecimal amount, String eventType) {
        OrderMessage orderMessage = OrderMessage.create(orderId, userId, status, amount, eventType);
        
        String key = "order-" + orderId;
        sendMessageAsync(ORDER_TOPIC, key, orderMessage);
        
        log.info("发送订单事件 - 订单ID: {}, 事件类型: {}, 状态: {}", orderId, eventType, status);
    }

    /**
     * 发送通知消息
     * 支持不同优先级的通知消息
     */
    public void sendNotification(Long userId, String type, String title, String content, String priority) {
        NotificationMessage notification = NotificationMessage.create(userId, type, title, content, priority);
        notification.setChannel("APP_PUSH");
        notification.setScheduledTime(LocalDateTime.now().plusMinutes(1)); // 1分钟后发送
        
        String key = "notification-" + userId;
        
        // 根据优先级选择不同的发送方式
        if ("HIGH".equals(priority)) {
            // 高优先级消息同步发送，确保立即处理
            sendMessageSync(NOTIFICATION_TOPIC, key, notification);
        } else {
            // 普通优先级消息异步发送
            sendMessageAsync(NOTIFICATION_TOPIC, key, notification);
        }
        
        log.info("发送通知消息 - 用户ID: {}, 类型: {}, 优先级: {}", userId, type, priority);
    }

    /**
     * 批量发送消息
     * 一次性发送多条相关消息
     */
    public void sendBatchMessages(String topic, List<Object> messages) {
        log.info("批量发送消息到Topic: {}, 消息数量: {}", topic, messages.size());
        
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
            messages.stream()
                .map(message -> {
                    String key = "batch-" + System.nanoTime();
                    return kafkaTemplate.send(topic, key, message)
                        .completable()
                        .thenAccept(result -> 
                            log.debug("批量消息发送成功 - Offset: {}", 
                                result.getRecordMetadata().offset()));
                })
                .toArray(CompletableFuture[]::new)
        );
        
        allFutures.thenRun(() -> 
            log.info("批量发送完成，共发送 {} 条消息", messages.size())
        ).exceptionally(throwable -> {
            log.error("批量发送失败", throwable);
            return null;
        });
    }

    /**
     * 发送延迟消息（模拟）
     * 通过消息头部标记延迟时间，由消费者处理延迟逻辑
     */
    public void sendDelayedMessage(String topic, String key, Object message, long delaySeconds) {
        log.info("发送延迟消息 - Topic: {}, Key: {}, 延迟: {}秒", topic, key, delaySeconds);
        
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, key, message);
        
        // 添加延迟处理的头部信息
        long executeTime = System.currentTimeMillis() + (delaySeconds * 1000);
        record.headers().add(new RecordHeader("execute-time", String.valueOf(executeTime).getBytes(StandardCharsets.UTF_8)));
        record.headers().add(new RecordHeader("delay-seconds", String.valueOf(delaySeconds).getBytes(StandardCharsets.UTF_8)));
        
        kafkaTemplate.send(record);
    }

    /**
     * 获取Topic信息（用于监控和调试）
     */
    public void logTopicInfo(String topic) {
        try {
            // 这里可以添加Topic元数据获取逻辑
            log.info("Topic信息 - 名称: {}", topic);
        } catch (Exception e) {
            log.error("获取Topic信息失败: {}", e.getMessage());
        }
    }
}