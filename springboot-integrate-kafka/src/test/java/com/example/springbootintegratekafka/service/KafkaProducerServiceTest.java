package com.example.springbootintegratekafka.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaProducerServiceTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Test
    public void testSendMessage() {
        // 测试发送消息功能
        assertNotNull("KafkaProducerService should not be null", kafkaProducerService);
        
        // 发送消息测试
        kafkaProducerService.sendMessage("test-topic", "Hello Kafka");
        
        // 验证服务实例存在
        assertTrue("KafkaProducerService instance should be valid", kafkaProducerService != null);
    }
    
    @Test
    public void testProducerServiceLoaded() {
        // 验证服务是否正确加载
        assertNotNull("KafkaProducerService should be autowired", kafkaProducerService);
    }
}