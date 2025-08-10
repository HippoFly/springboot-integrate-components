package com.example.springbootintegratekafka;

import com.example.springbootintegratekafka.service.KafkaProducerService;
import com.example.springbootintegratekafka.service.KafkaConsumerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
        // 验证Spring上下文是否正确加载
        assertNotNull("Application context should not be null", context);
    }

    @Test
    public void testKafkaServicesAreLoaded() {
        // 验证Kafka服务是否正确加载
        KafkaProducerService kafkaProducerService = context.getBean(KafkaProducerService.class);
        KafkaConsumerService kafkaConsumerService = context.getBean(KafkaConsumerService.class);
        
        assertNotNull("KafkaProducerService should be loaded", kafkaProducerService);
        assertNotNull("KafkaConsumerService should be loaded", kafkaConsumerService);
        assertTrue("KafkaProducerService should be instance of KafkaProducerService", 
                  kafkaProducerService instanceof KafkaProducerService);
        assertTrue("KafkaConsumerService should be instance of KafkaConsumerService", 
                  kafkaConsumerService instanceof KafkaConsumerService);
    }
}