package com.example.springbootintegratekafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户消息实体类
 * 展示Kafka中复杂对象的序列化和反序列化
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage {
    
    /**
     * 消息ID - 用于消息去重和追踪
     */
    private String messageId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 操作类型：CREATE, UPDATE, DELETE
     */
    private String operation;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 部门ID
     */
    private Long departmentId;
    
    /**
     * 消息时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * 消息来源系统
     */
    private String source;
    
    /**
     * 消息版本 - 用于消息格式演进
     */
    private String version;
    
    /**
     * 扩展属性 - 用于存储额外信息
     */
    private String metadata;
    
    /**
     * 创建用户消息的便捷方法
     */
    public static UserMessage create(Long userId, String username, String operation, String content) {
        UserMessage message = new UserMessage();
        message.setMessageId(java.util.UUID.randomUUID().toString());
        message.setUserId(userId);
        message.setUsername(username);
        message.setOperation(operation);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setSource("user-service");
        message.setVersion("1.0");
        return message;
    }
}
