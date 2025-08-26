package com.example.springbootintegratekafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 通知消息实体类
 * 展示Kafka中通知系统的消息设计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    
    /**
     * 消息ID
     */
    private String messageId;
    
    /**
     * 接收用户ID
     */
    private Long userId;
    
    /**
     * 通知类型：EMAIL, SMS, PUSH, SYSTEM
     */
    private String type;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 优先级：HIGH, MEDIUM, LOW
     */
    private String priority;
    
    /**
     * 通知渠道：EMAIL, SMS, APP_PUSH, WEB_PUSH
     */
    private String channel;
    
    /**
     * 消息时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * 计划发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledTime;
    
    /**
     * 扩展参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 模板ID
     */
    private String templateId;
    
    /**
     * 创建通知消息的便捷方法
     */
    public static NotificationMessage create(Long userId, String type, String title, String content, String priority) {
        NotificationMessage message = new NotificationMessage();
        message.setMessageId(java.util.UUID.randomUUID().toString());
        message.setUserId(userId);
        message.setType(type);
        message.setTitle(title);
        message.setContent(content);
        message.setPriority(priority);
        message.setTimestamp(LocalDateTime.now());
        return message;
    }
}
