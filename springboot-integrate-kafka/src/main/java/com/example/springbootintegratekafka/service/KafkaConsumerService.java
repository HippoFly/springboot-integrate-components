package com.example.springbootintegratekafka.service;

import com.example.springbootintegratekafka.model.NotificationMessage;
import com.example.springbootintegratekafka.model.OrderMessage;
import com.example.springbootintegratekafka.model.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Kafka消费者服务
 * 展示Kafka消息消费的各种模式和特性
 * 
 * 主要功能：
 * 1. 基础消息消费 - 简单的消息监听
 * 2. 类型安全消费 - 强类型消息消费
 * 3. 手动确认消费 - 手动控制offset提交
 * 4. 批量消息消费 - 一次处理多条消息
 * 5. 分区指定消费 - 消费指定分区的消息
 * 6. 消息头部处理 - 处理消息头部信息
 * 7. 错误处理重试 - 消费失败时的重试机制
 * 8. 延迟消息处理 - 处理延迟消息逻辑
 */
@Slf4j
@Service
public class KafkaConsumerService {

    /**
     * 基础消息消费
     * 最简单的消息监听方式
     */
    @KafkaListener(topics = "general-events", groupId = "general-consumer-group")
    public void listenGeneralEvents(String message) {
        log.info("收到通用消息: {}", message);
        
        // 模拟消息处理
        try {
            processGeneralMessage(message);
            log.debug("通用消息处理完成");
        } catch (Exception e) {
            log.error("通用消息处理失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 用户消息消费（类型安全）
     * 展示强类型消息消费和手动确认
     */
    @KafkaListener(
        topics = "user-events",
        groupId = "user-consumer-group",
        containerFactory = "userMessageKafkaListenerContainerFactory"
    )
    public void listenUserEvents(
        @Payload UserMessage userMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
        @Header(KafkaHeaders.OFFSET) Long offset,
        Acknowledgment acknowledgment
    ) {
        log.info("收到用户事件 - Topic: {}, Partition: {}, Offset: {}", topic, partition, offset);
        log.info("用户消息详情: ID={}, 用户名={}, 操作={}", 
            userMessage.getUserId(), userMessage.getUsername(), userMessage.getOperation());
        
        try {
            // 处理用户事件
            processUserEvent(userMessage);
            
            // 手动确认消息处理完成
            acknowledgment.acknowledge();
            log.debug("用户事件处理完成并已确认");
            
        } catch (Exception e) {
            log.error("用户事件处理失败 - 用户ID: {}, 错误: {}", 
                userMessage.getUserId(), e.getMessage(), e);
            // 不确认消息，让Kafka重新投递
        }
    }

    /**
     * 订单消息消费（带重试机制）
     * 展示消费失败时的重试处理
     */
    @KafkaListener(
        topics = "order-events",
        groupId = "order-consumer-group",
        containerFactory = "orderMessageKafkaListenerContainerFactory"
    )
    @Retryable(
        value = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void listenOrderEvents(
        @Payload OrderMessage orderMessage,
        ConsumerRecord<String, OrderMessage> record,
        Acknowledgment acknowledgment
    ) {
        log.info("收到订单事件 - 订单ID: {}, 事件类型: {}, 状态: {}", 
            orderMessage.getOrderId(), orderMessage.getEventType(), orderMessage.getStatus());
        
        try {
            // 模拟订单处理可能失败的情况
            if (orderMessage.getAmount().doubleValue() < 0) {
                throw new IllegalArgumentException("订单金额不能为负数");
            }
            
            processOrderEvent(orderMessage);
            acknowledgment.acknowledge();
            log.info("订单事件处理成功 - 订单ID: {}", orderMessage.getOrderId());
            
        } catch (Exception e) {
            log.error("订单事件处理失败 - 订单ID: {}, 重试中...", orderMessage.getOrderId(), e);
            throw e; // 重新抛出异常触发重试
        }
    }

    /**
     * 通知消息消费（优先级处理）
     * 展示根据消息优先级的不同处理方式
     */
    @KafkaListener(
        topics = "notification-events",
        groupId = "notification-consumer-group",
        containerFactory = "notificationMessageKafkaListenerContainerFactory"
    )
    public void listenNotificationEvents(
        @Payload NotificationMessage notification,
        @Header(value = "source", required = false) String source,
        @Header(value = "version", required = false) String version,
        Acknowledgment acknowledgment
    ) {
        log.info("收到通知消息 - 用户ID: {}, 类型: {}, 优先级: {}", 
            notification.getUserId(), notification.getType(), notification.getPriority());
        
        try {
            // 根据优先级采用不同的处理策略
            if ("HIGH".equals(notification.getPriority())) {
                processHighPriorityNotification(notification);
            } else {
                processNormalNotification(notification);
            }
            
            acknowledgment.acknowledge();
            log.debug("通知消息处理完成 - 消息ID: {}", notification.getMessageId());
            
        } catch (Exception e) {
            log.error("通知消息处理失败 - 消息ID: {}, 错误: {}", 
                notification.getMessageId(), e.getMessage(), e);
        }
    }

    /**
     * 批量消息消费
     * 一次性处理多条消息，提高处理效率
     */
    @KafkaListener(
        topics = "batch-events",
        groupId = "batch-consumer-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenBatchEvents(
        List<ConsumerRecord<String, Object>> records,
        Acknowledgment acknowledgment
    ) {
        log.info("收到批量消息，数量: {}", records.size());
        
        int successCount = 0;
        int failCount = 0;
        
        for (ConsumerRecord<String, Object> record : records) {
            try {
                log.debug("处理批量消息 - Key: {}, Partition: {}, Offset: {}", 
                    record.key(), record.partition(), record.offset());
                
                processBatchMessage(record.value());
                successCount++;
                
            } catch (Exception e) {
                log.error("批量消息处理失败 - Key: {}, 错误: {}", record.key(), e.getMessage());
                failCount++;
            }
        }
        
        log.info("批量消息处理完成 - 成功: {}, 失败: {}", successCount, failCount);
        acknowledgment.acknowledge();
    }

    /**
     * 指定分区消费
     * 只消费特定分区的消息
     */
    @KafkaListener(
        topicPartitions = @TopicPartition(
            topic = "partition-events",
            partitions = {"0", "1"}
        ),
        groupId = "partition-consumer-group"
    )
    public void listenSpecificPartitions(
        ConsumerRecord<String, Object> record,
        Acknowledgment acknowledgment
    ) {
        log.info("收到分区消息 - Partition: {}, Key: {}, Offset: {}", 
            record.partition(), record.key(), record.offset());
        
        try {
            processPartitionMessage(record.value(), record.partition());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("分区消息处理失败 - Partition: {}, 错误: {}", record.partition(), e.getMessage());
        }
    }

    /**
     * 从指定偏移量开始消费
     * 用于消息重放或特定位置开始消费
     */
    @KafkaListener(
        topicPartitions = @TopicPartition(
            topic = "replay-events",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0")
        ),
        groupId = "replay-consumer-group"
    )
    public void listenFromSpecificOffset(
        ConsumerRecord<String, Object> record,
        Acknowledgment acknowledgment
    ) {
        log.info("收到重放消息 - Offset: {}, Timestamp: {}", 
            record.offset(), record.timestamp());
        
        try {
            processReplayMessage(record.value());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("重放消息处理失败 - Offset: {}, 错误: {}", record.offset(), e.getMessage());
        }
    }

    /**
     * 延迟消息处理
     * 处理带有延迟执行时间的消息
     */
    @KafkaListener(
        topics = "delayed-events",
        groupId = "delayed-consumer-group"
    )
    public void listenDelayedEvents(
        ConsumerRecord<String, Object> record,
        Acknowledgment acknowledgment
    ) {
        // 检查消息头部中的执行时间
        org.apache.kafka.common.header.Header executeTimeHeader = record.headers().lastHeader("execute-time");
        
        if (executeTimeHeader != null) {
            long executeTime = Long.parseLong(new String(executeTimeHeader.value()));
            long currentTime = System.currentTimeMillis();
            
            if (currentTime < executeTime) {
                long delayMs = executeTime - currentTime;
                log.info("延迟消息未到执行时间，等待 {} 毫秒", delayMs);
                
                try {
                    TimeUnit.MILLISECONDS.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("延迟等待被中断");
                    return;
                }
            }
        }
        
        log.info("处理延迟消息 - Key: {}", record.key());
        
        try {
            processDelayedMessage(record.value());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("延迟消息处理失败 - Key: {}, 错误: {}", record.key(), e.getMessage());
        }
    }

    /**
     * 死信队列消息处理
     * 处理多次重试失败的消息
     */
    @KafkaListener(
        topics = "dead-letter-queue",
        groupId = "dlq-consumer-group"
    )
    public void listenDeadLetterQueue(
        ConsumerRecord<String, Object> record,
        Acknowledgment acknowledgment
    ) {
        log.warn("收到死信队列消息 - Key: {}, 原始Topic: {}", 
            record.key(), getOriginalTopic(record));
        
        try {
            // 记录死信消息，用于后续分析
            logDeadLetterMessage(record);
            
            // 可以选择人工处理或者丢弃
            processDeadLetterMessage(record.value());
            
            acknowledgment.acknowledge();
            log.info("死信消息处理完成 - Key: {}", record.key());
            
        } catch (Exception e) {
            log.error("死信消息处理失败 - Key: {}, 错误: {}", record.key(), e.getMessage());
            // 死信消息处理失败时也要确认，避免无限循环
            acknowledgment.acknowledge();
        }
    }

    // ==================== 私有处理方法 ====================

    private void processGeneralMessage(String message) {
        // 模拟通用消息处理逻辑
        log.debug("处理通用消息: {}", message);
    }

    private void processUserEvent(UserMessage userMessage) {
        // 模拟用户事件处理逻辑
        switch (userMessage.getOperation()) {
            case "CREATE":
                log.info("处理用户创建事件 - 用户ID: {}", userMessage.getUserId());
                break;
            case "UPDATE":
                log.info("处理用户更新事件 - 用户ID: {}", userMessage.getUserId());
                break;
            case "DELETE":
                log.info("处理用户删除事件 - 用户ID: {}", userMessage.getUserId());
                break;
            default:
                log.warn("未知的用户操作类型: {}", userMessage.getOperation());
        }
    }

    private void processOrderEvent(OrderMessage orderMessage) {
        // 模拟订单事件处理逻辑
        switch (orderMessage.getEventType()) {
            case "ORDER_CREATED":
                log.info("处理订单创建事件 - 订单ID: {}", orderMessage.getOrderId());
                break;
            case "ORDER_UPDATED":
                log.info("处理订单更新事件 - 订单ID: {}", orderMessage.getOrderId());
                break;
            case "ORDER_CANCELLED":
                log.info("处理订单取消事件 - 订单ID: {}", orderMessage.getOrderId());
                break;
            default:
                log.warn("未知的订单事件类型: {}", orderMessage.getEventType());
        }
    }

    private void processHighPriorityNotification(NotificationMessage notification) {
        // 高优先级通知立即处理
        log.info("立即处理高优先级通知 - 用户ID: {}, 标题: {}", 
            notification.getUserId(), notification.getTitle());
        
        // 模拟立即发送逻辑
        sendImmediateNotification(notification);
    }

    private void processNormalNotification(NotificationMessage notification) {
        // 普通优先级通知可以延迟处理
        log.info("处理普通优先级通知 - 用户ID: {}, 标题: {}", 
            notification.getUserId(), notification.getTitle());
        
        // 检查是否有计划发送时间
        if (notification.getScheduledTime() != null && 
            notification.getScheduledTime().isAfter(LocalDateTime.now())) {
            log.info("通知消息计划在 {} 发送", notification.getScheduledTime());
            // 这里可以实现延迟发送逻辑
        }
    }

    private void processBatchMessage(Object message) {
        // 模拟批量消息处理
        log.debug("处理批量消息项: {}", message);
    }

    private void processPartitionMessage(Object message, int partition) {
        // 模拟分区消息处理
        log.debug("处理分区 {} 的消息: {}", partition, message);
    }

    private void processReplayMessage(Object message) {
        // 模拟重放消息处理
        log.debug("处理重放消息: {}", message);
    }

    private void processDelayedMessage(Object message) {
        // 模拟延迟消息处理
        log.info("执行延迟消息处理: {}", message);
    }

    private void processDeadLetterMessage(Object message) {
        // 模拟死信消息处理
        log.warn("处理死信消息: {}", message);
    }

    private void sendImmediateNotification(NotificationMessage notification) {
        // 模拟立即发送通知
        log.info("立即发送通知给用户 {} - {}", notification.getUserId(), notification.getTitle());
    }

    private String getOriginalTopic(ConsumerRecord<String, Object> record) {
        org.apache.kafka.common.header.Header originalTopicHeader = record.headers().lastHeader("original-topic");
        if (originalTopicHeader != null) {
            return new String(originalTopicHeader.value());
        }
        return "unknown";
    }

    private void logDeadLetterMessage(ConsumerRecord<String, Object> record) {
        // 记录死信消息详情用于分析
        log.warn("死信消息详情 - Topic: {}, Partition: {}, Offset: {}, Key: {}", 
            record.topic(), record.partition(), record.offset(), record.key());
    }
}