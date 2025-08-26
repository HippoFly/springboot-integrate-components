package com.example.springbootintegratekafka.controller;

import com.example.springbootintegratekafka.service.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kafka控制器
 * 提供Kafka消息发送和监控的REST API
 * 
 * 主要功能：
 * 1. 基础消息发送 - 简单消息发送接口
 * 2. 业务消息发送 - 用户、订单、通知等业务消息
 * 3. 高级消息发送 - 事务、批量、延迟消息
 * 4. 消息监控 - Topic信息、消费者状态等
 * 5. 测试工具 - 压力测试、消息模拟等
 */
@Slf4j
@RestController
@RequestMapping("/api/kafka")
@Tag(name = "Kafka消息服务", description = "Kafka消息发送和监控API")
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // ==================== 基础消息发送 ====================

    /**
     * 发送简单消息
     */
    @PostMapping("/send/simple")
    @Operation(summary = "发送简单消息", description = "发送字符串消息到指定Topic")
    public ResponseEntity<Map<String, Object>> sendSimpleMessage(
        @Parameter(description = "Topic名称") @RequestParam String topic,
        @Parameter(description = "消息内容") @RequestParam String message
    ) {
        log.info("API调用 - 发送简单消息到Topic: {}", topic);
        
        kafkaProducerService.sendMessage(topic, message);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "消息发送成功");
        response.put("topic", topic);
        response.put("content", message);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 发送带Key的消息
     */
    @PostMapping("/send/with-key")
    @Operation(summary = "发送带Key的消息", description = "发送带分区Key的消息，相同Key会发送到同一分区")
    public ResponseEntity<Map<String, Object>> sendMessageWithKey(
        @Parameter(description = "Topic名称") @RequestParam String topic,
        @Parameter(description = "消息Key") @RequestParam String key,
        @Parameter(description = "消息内容") @RequestParam String message
    ) {
        log.info("API调用 - 发送带Key的消息到Topic: {}, Key: {}", topic, key);
        
        kafkaProducerService.sendMessageWithKey(topic, key, message);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "带Key消息发送成功");
        response.put("topic", topic);
        response.put("key", key);
        response.put("content", message);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 同步发送消息
     */
    @PostMapping("/send/sync")
    @Operation(summary = "同步发送消息", description = "同步发送消息并返回发送结果")
    public ResponseEntity<Map<String, Object>> sendMessageSync(
        @Parameter(description = "Topic名称") @RequestParam String topic,
        @Parameter(description = "消息Key") @RequestParam String key,
        @Parameter(description = "消息内容") @RequestParam String message
    ) {
        log.info("API调用 - 同步发送消息到Topic: {}, Key: {}", topic, key);
        
        try {
            SendResult<String, Object> result = kafkaProducerService.sendMessageSync(topic, key, message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "同步发送成功");
            response.put("topic", result.getRecordMetadata().topic());
            response.put("partition", result.getRecordMetadata().partition());
            response.put("offset", result.getRecordMetadata().offset());
            response.put("timestamp", result.getRecordMetadata().timestamp());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("同步发送失败", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "同步发送失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ==================== 业务消息发送 ====================

    /**
     * 发送用户事件消息
     */
    @PostMapping("/send/user-event")
    @Operation(summary = "发送用户事件", description = "发送用户相关的业务事件消息")
    public ResponseEntity<Map<String, Object>> sendUserEvent(
        @Parameter(description = "用户ID") @RequestParam Long userId,
        @Parameter(description = "用户名") @RequestParam String username,
        @Parameter(description = "操作类型") @RequestParam String operation,
        @Parameter(description = "操作内容") @RequestParam String content
    ) {
        log.info("API调用 - 发送用户事件: 用户ID={}, 操作={}", userId, operation);
        
        kafkaProducerService.sendUserEvent(userId, username, operation, content);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "用户事件发送成功");
        response.put("userId", userId);
        response.put("operation", operation);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 发送订单事件消息
     */
    @PostMapping("/send/order-event")
    @Operation(summary = "发送订单事件", description = "发送订单相关的业务事件消息")
    public ResponseEntity<Map<String, Object>> sendOrderEvent(
        @Parameter(description = "订单ID") @RequestParam String orderId,
        @Parameter(description = "用户ID") @RequestParam Long userId,
        @Parameter(description = "订单状态") @RequestParam String status,
        @Parameter(description = "订单金额") @RequestParam BigDecimal amount,
        @Parameter(description = "事件类型") @RequestParam String eventType
    ) {
        log.info("API调用 - 发送订单事件: 订单ID={}, 事件类型={}", orderId, eventType);
        
        kafkaProducerService.sendOrderEvent(orderId, userId, status, amount, eventType);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "订单事件发送成功");
        response.put("orderId", orderId);
        response.put("eventType", eventType);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 发送通知消息
     */
    @PostMapping("/send/notification")
    @Operation(summary = "发送通知消息", description = "发送用户通知消息")
    public ResponseEntity<Map<String, Object>> sendNotification(
        @Parameter(description = "用户ID") @RequestParam Long userId,
        @Parameter(description = "通知类型") @RequestParam String type,
        @Parameter(description = "通知标题") @RequestParam String title,
        @Parameter(description = "通知内容") @RequestParam String content,
        @Parameter(description = "优先级") @RequestParam(defaultValue = "MEDIUM") String priority
    ) {
        log.info("API调用 - 发送通知消息: 用户ID={}, 类型={}, 优先级={}", userId, type, priority);
        
        kafkaProducerService.sendNotification(userId, type, title, content, priority);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "通知消息发送成功");
        response.put("userId", userId);
        response.put("type", type);
        response.put("priority", priority);
        
        return ResponseEntity.ok(response);
    }

    // ==================== 高级消息发送 ====================

    /**
     * 发送事务消息
     */
    @PostMapping("/send/transaction")
    @Operation(summary = "发送事务消息", description = "在事务中发送多条消息，要么全部成功要么全部失败")
    public ResponseEntity<Map<String, Object>> sendTransactionMessages(
        @Parameter(description = "消息列表") @RequestBody List<String> messages
    ) {
        log.info("API调用 - 发送事务消息，数量: {}", messages.size());
        
        try {
            kafkaProducerService.sendMessagesInTransaction(Arrays.asList(messages.toArray()));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "事务消息发送成功");
            response.put("count", messages.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("事务消息发送失败", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "事务消息发送失败: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 发送批量消息
     */
    @PostMapping("/send/batch")
    @Operation(summary = "发送批量消息", description = "批量发送多条消息到指定Topic")
    public ResponseEntity<Map<String, Object>> sendBatchMessages(
        @Parameter(description = "Topic名称") @RequestParam String topic,
        @Parameter(description = "消息列表") @RequestBody List<String> messages
    ) {
        log.info("API调用 - 批量发送消息到Topic: {}, 数量: {}", topic, messages.size());
        
        kafkaProducerService.sendBatchMessages(topic, Arrays.asList(messages.toArray()));
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "批量消息发送成功");
        response.put("topic", topic);
        response.put("count", messages.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 发送延迟消息
     */
    @PostMapping("/send/delayed")
    @Operation(summary = "发送延迟消息", description = "发送延迟执行的消息")
    public ResponseEntity<Map<String, Object>> sendDelayedMessage(
        @Parameter(description = "Topic名称") @RequestParam String topic,
        @Parameter(description = "消息Key") @RequestParam String key,
        @Parameter(description = "消息内容") @RequestParam String message,
        @Parameter(description = "延迟秒数") @RequestParam long delaySeconds
    ) {
        log.info("API调用 - 发送延迟消息: Topic={}, 延迟={}秒", topic, delaySeconds);
        
        kafkaProducerService.sendDelayedMessage(topic, key, message, delaySeconds);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "延迟消息发送成功");
        response.put("topic", topic);
        response.put("delaySeconds", delaySeconds);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 发送到指定分区
     */
    @PostMapping("/send/partition")
    @Operation(summary = "发送到指定分区", description = "发送消息到指定的分区")
    public ResponseEntity<Map<String, Object>> sendToPartition(
        @Parameter(description = "Topic名称") @RequestParam String topic,
        @Parameter(description = "分区号") @RequestParam Integer partition,
        @Parameter(description = "消息Key") @RequestParam String key,
        @Parameter(description = "消息内容") @RequestParam String message
    ) {
        log.info("API调用 - 发送消息到指定分区: Topic={}, Partition={}", topic, partition);
        
        kafkaProducerService.sendMessageToPartition(topic, partition, key, message);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "分区消息发送成功");
        response.put("topic", topic);
        response.put("partition", partition);
        
        return ResponseEntity.ok(response);
    }

    // ==================== 监控和工具 ====================

    /**
     * 获取Topic信息
     */
    @GetMapping("/info/topic")
    @Operation(summary = "获取Topic信息", description = "获取指定Topic的基本信息")
    public ResponseEntity<Map<String, Object>> getTopicInfo(
        @Parameter(description = "Topic名称") @RequestParam String topic
    ) {
        log.info("API调用 - 获取Topic信息: {}", topic);
        
        kafkaProducerService.logTopicInfo(topic);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("topic", topic);
        response.put("message", "Topic信息已记录到日志");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查Kafka服务是否正常")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.info("API调用 - Kafka健康检查");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Kafka");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 压力测试
     */
    @PostMapping("/test/stress")
    @Operation(summary = "压力测试", description = "发送大量消息进行压力测试")
    public ResponseEntity<Map<String, Object>> stressTest(
        @Parameter(description = "Topic名称") @RequestParam String topic,
        @Parameter(description = "消息数量") @RequestParam(defaultValue = "100") int messageCount,
        @Parameter(description = "消息前缀") @RequestParam(defaultValue = "test") String messagePrefix
    ) {
        log.info("API调用 - 压力测试: Topic={}, 数量={}", topic, messageCount);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < messageCount; i++) {
            String message = messagePrefix + "-" + i;
            String key = "stress-" + i;
            kafkaProducerService.sendMessageWithKey(topic, key, message);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "压力测试完成");
        response.put("topic", topic);
        response.put("messageCount", messageCount);
        response.put("duration", duration + "ms");
        response.put("throughput", (messageCount * 1000.0 / duration) + " msg/s");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取可用的Topic列表
     */
    @GetMapping("/topics")
    @Operation(summary = "获取Topic列表", description = "获取系统中定义的Topic常量")
    public ResponseEntity<Map<String, Object>> getTopics() {
        log.info("API调用 - 获取Topic列表");
        
        Map<String, String> topics = new HashMap<>();
        topics.put("USER_TOPIC", KafkaProducerService.USER_TOPIC);
        topics.put("ORDER_TOPIC", KafkaProducerService.ORDER_TOPIC);
        topics.put("NOTIFICATION_TOPIC", KafkaProducerService.NOTIFICATION_TOPIC);
        topics.put("GENERAL_TOPIC", KafkaProducerService.GENERAL_TOPIC);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("topics", topics);
        response.put("count", topics.size());
        
        return ResponseEntity.ok(response);
    }
}