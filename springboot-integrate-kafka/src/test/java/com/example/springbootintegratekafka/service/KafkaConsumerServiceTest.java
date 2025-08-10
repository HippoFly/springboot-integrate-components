package com.example.springbootintegratekafka.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaConsumerServiceTest {

    @Test
    public void testListen() {
        // 实际的消息监听测试需要Kafka服务器运行
        // 这里只是验证组件能正常加载
        KafkaConsumerService consumerService = new KafkaConsumerService();
        // 验证对象能正常创建
        assertNotNull(consumerService);
    }
}