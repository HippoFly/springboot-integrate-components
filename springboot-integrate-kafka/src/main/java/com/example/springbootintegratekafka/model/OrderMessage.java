package com.example.springbootintegratekafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单消息实体类
 * 展示Kafka中业务事件的消息设计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage {
    
    /**
     * 消息ID
     */
    private String messageId;
    
    /**
     * 订单ID
     */
    private String orderId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单状态：CREATED, PAID, SHIPPED, DELIVERED, CANCELLED
     */
    private String status;
    
    /**
     * 订单金额
     */
    private BigDecimal amount;
    
    /**
     * 订单商品列表
     */
    private List<OrderItem> items;
    
    /**
     * 订单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;
    
    /**
     * 消息时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * 事件类型：ORDER_CREATED, ORDER_UPDATED, ORDER_CANCELLED
     */
    private String eventType;
    
    /**
     * 订单商品项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
    }
    
    /**
     * 创建订单消息的便捷方法
     */
    public static OrderMessage create(String orderId, Long userId, String status, BigDecimal amount, String eventType) {
        OrderMessage message = new OrderMessage();
        message.setMessageId(java.util.UUID.randomUUID().toString());
        message.setOrderId(orderId);
        message.setUserId(userId);
        message.setStatus(status);
        message.setAmount(amount);
        message.setEventType(eventType);
        message.setOrderTime(LocalDateTime.now());
        message.setTimestamp(LocalDateTime.now());
        return message;
    }
}
