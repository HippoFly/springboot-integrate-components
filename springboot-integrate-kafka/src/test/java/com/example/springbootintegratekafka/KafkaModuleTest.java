package com.example.springbootintegratekafka;

import com.example.springbootintegratekafka.model.UserMessage;
import com.example.springbootintegratekafka.model.OrderMessage;
import com.example.springbootintegratekafka.model.NotificationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Kafka模块功能测试
 * 验证消息实体类的基本功能
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=localhost:9092",
    "spring.kafka.consumer.auto-offset-reset=earliest"
})
public class KafkaModuleTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    public void testUserMessageSerialization() throws Exception {
        // 测试UserMessage序列化和反序列化
        UserMessage userMessage = new UserMessage();
        userMessage.setMessageId("msg123");
        userMessage.setUserId(123L);
        userMessage.setUsername("张三");
        userMessage.setOperation("CREATE");
        userMessage.setContent("用户注册");
        userMessage.setDepartmentId(1L);
        userMessage.setTimestamp(LocalDateTime.now());
        
        // 序列化
        String json = objectMapper.writeValueAsString(userMessage);
        assertNotNull(json);
        assertTrue(json.contains("张三"));
        assertTrue(json.contains("CREATE"));
        
        // 反序列化
        UserMessage deserializedMessage = objectMapper.readValue(json, UserMessage.class);
        assertEquals(123L, deserializedMessage.getUserId());
        assertEquals("张三", deserializedMessage.getUsername());
        assertEquals("CREATE", deserializedMessage.getOperation());
    }

    @Test
    public void testOrderMessageSerialization() throws Exception {
        // 测试OrderMessage序列化和反序列化
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setMessageId("order-msg-123");
        orderMessage.setOrderId("order123");
        orderMessage.setUserId(456L);
        orderMessage.setTimestamp(LocalDateTime.now());
        
        // 序列化
        String json = objectMapper.writeValueAsString(orderMessage);
        assertNotNull(json);
        assertTrue(json.contains("order123"));
        
        // 反序列化
        OrderMessage deserializedMessage = objectMapper.readValue(json, OrderMessage.class);
        assertEquals("order123", deserializedMessage.getOrderId());
        assertEquals(456L, deserializedMessage.getUserId());
    }

    @Test
    public void testNotificationMessageSerialization() throws Exception {
        // 测试NotificationMessage序列化和反序列化
        NotificationMessage notification = new NotificationMessage();
        notification.setMessageId("notif-123");
        notification.setUserId(789L);
        notification.setTitle("订单确认");
        notification.setContent("您的订单已确认");
        notification.setPriority("HIGH");
        notification.setTimestamp(LocalDateTime.now());
        
        // 序列化
        String json = objectMapper.writeValueAsString(notification);
        assertNotNull(json);
        assertTrue(json.contains("订单确认"));
        
        // 反序列化
        NotificationMessage deserializedMessage = objectMapper.readValue(json, NotificationMessage.class);
        assertEquals(789L, deserializedMessage.getUserId());
        assertEquals("订单确认", deserializedMessage.getTitle());
    }

    @Test
    public void testMessageBasicFunctionality() {
        // 测试消息基本功能
        UserMessage userMessage = new UserMessage();
        userMessage.setMessageId("msg123");
        userMessage.setUserId(123L);
        userMessage.setUsername("张三");
        userMessage.setOperation("CREATE");
        userMessage.setTimestamp(LocalDateTime.now());
        
        assertNotNull(userMessage.getMessageId());
        assertNotNull(userMessage.getTimestamp());
        assertEquals("张三", userMessage.getUsername());
        assertEquals("CREATE", userMessage.getOperation());
    }

    @Test
    public void testOrderMessageFields() {
        // 测试OrderMessage字段
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId("order123");
        orderMessage.setUserId(456L);
        
        assertEquals("order123", orderMessage.getOrderId());
        assertEquals(456L, orderMessage.getUserId());
    }

    @Test
    public void testNotificationMessageFields() {
        // 测试NotificationMessage字段
        NotificationMessage notification = new NotificationMessage();
        notification.setUserId(123L);
        notification.setTitle("提醒");
        notification.setContent("这是一条提醒消息");
        notification.setPriority("MEDIUM");
        
        assertEquals(123L, notification.getUserId());
        assertEquals("提醒", notification.getTitle());
        assertEquals("MEDIUM", notification.getPriority());
    }
}
